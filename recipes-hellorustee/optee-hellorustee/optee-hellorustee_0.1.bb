SUMMARY = "OP-TEE hello_rustee"
HOMEPAGE = "https://github.com/Zondax/hello-rustee"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "optee-client virtual/optee-os python3-pycrypto-native python3-pycryptodomex-native libgcc"
DEPENDS_stm32mp1 = "optee-client virtual/optee-os python3-pycrypto-native python3-pycryptodomex-native libgcc"
DEPENDS_imx8mqevk = "optee-client-imx optee-os-imx python3-pycrypto-native python3-pycryptodomex-native libgcc"
DEPENDS_pico-imx8mq = "optee-client optee-os python3-pycrypto-native python3-pycryptodomex-native libgcc"
DEPENDS_flex-imx8mm = "optee-client optee-os python3-pycrypto-native python3-pycryptodomex-native libgcc"

inherit cargo python3native

SRC_URI = "gitsm://github.com/Zondax/hello-rustee.git;branch=rustee_app;rev=rustee_app \
           file://0001-make-do-not-force-deps-fetching.patch \
           file://0001-make-do-not-force-qemu-selection.patch \
           file://0002-make-use-proper-Rust-target.patch \
           file://0003-crates-remove-broken-union-alignment.patch \
           "

S = "${WORKDIR}/git"
B = "${S}"

CARGO_DISABLE_BITBAKE_VENDORING = "1"

OPTEE_CLIENT_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TEEC_EXPORT = "${STAGING_DIR_HOST}${prefix}"

# stm32mp1 or qemu_v7
EXTRA_OEMAKE = " \
    TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta \
    OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
    TEEC_EXPORT=${TEEC_EXPORT} \
    HOST_CROSS_COMPILE=${TARGET_PREFIX} \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    RUST_TARGET=${TARGET_SYS} \
    LIBGCC_LOCATE_CFLAGS='--sysroot=${STAGING_DIR_HOST}' \
    V=1 \
    "

EXTRA_OEMAKE_qemu-optee64 = " \
    TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta \
    OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
    TEEC_EXPORT=${TEEC_EXPORT} \
    HOST_CROSS_COMPILE=${TARGET_PREFIX} \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    RUST_TARGET=${TARGET_SYS} \
    LIBGCC_LOCATE_CFLAGS='--sysroot=${STAGING_DIR_HOST}' \
    V=1 \
    "

EXTRA_OEMAKE_imx8mqevk = " \
    TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta_arm64 \
    OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
    TEEC_EXPORT=${TEEC_EXPORT} \
    HOST_CROSS_COMPILE=${TARGET_PREFIX} \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    RUST_TARGET=${TARGET_SYS} \
    LIBGCC_LOCATE_CFLAGS='--sysroot=${STAGING_DIR_HOST}' \
    V=1 \
    "

EXTRA_OEMAKE_flex-imx8mm = " \
    TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta_arm64 \
    OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
    TEEC_EXPORT=${TEEC_EXPORT} \
    HOST_CROSS_COMPILE=${TARGET_PREFIX} \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    RUST_TARGET=${TARGET_SYS} \
    LIBGCC_LOCATE_CFLAGS='--sysroot=${STAGING_DIR_HOST}' \
    V=1 \
    "

EXTRA_OEMAKE_pico-imx8mq = " \
    TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta_arm64 \
    OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
    TEEC_EXPORT=${TEEC_EXPORT} \
    HOST_CROSS_COMPILE=${TARGET_PREFIX} \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    RUST_TARGET=${TARGET_SYS} \
    LIBGCC_LOCATE_CFLAGS='--sysroot=${STAGING_DIR_HOST}' \
    V=1 \
    "

do_compile() {
    export RUSTFLAGS="${RUSTFLAGS}"
    export RUST_TARGET_PATH="${RUST_TARGET_PATH}"
    oe_runmake
}

do_install () {
    mkdir -p ${D}${nonarch_base_libdir}/optee_armtz
    mkdir -p ${D}${bindir}
    install -m 755 ${S}/framework/host/src/rustee_app ${D}${bindir}/
    install -m 444 ${S}/framework/ta/src/*.ta ${D}${nonarch_base_libdir}/optee_armtz
}

FILES_${PN} += "${nonarch_base_libdir}/optee_armtz/"

INSANE_SKIP_${PN} = "ldflags"
INSANE_SKIP_${PN}-dev = "ldflags"

# Imports machine specific configs from staging to build
PACKAGE_ARCH = "${MACHINE_ARCH}"
