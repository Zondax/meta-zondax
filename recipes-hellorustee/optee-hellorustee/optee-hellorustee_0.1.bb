SUMMARY = "OP-TEE hello_rustee"
HOMEPAGE = "https://github.com/Zondax/hello-rustee"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "optee-client optee-os python3-pycrypto-native python3-pycryptodomex-native libgcc"
DEPENDS_imx8mqevk = "optee-client-imx optee-os-imx python3-pycrypto-native libgcc"
DEPENDS_pico-imx8mq = "optee-client optee-os python3-pycrypto-native libgcc"
DEPENDS_flex-imx8mm = "optee-client optee-os python3-pycrypto-native libgcc"
DEPENDS_stm32mp1 = "optee-client optee-os python3-pycrypto-native libgcc"

inherit python3native

SRC_URI = "git://github.com/Zondax/hello-rustee.git;rev=master"
# or use SRC_URI = "file:///home/zondax/hello-rustee.git/src"

S = "${WORKDIR}/git/src"

OPTEE_CLIENT_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TEEC_EXPORT = "${STAGING_DIR_HOST}${prefix}"

# stm32mp1 or qemu_v7
EXTRA_OEMAKE = " \
    TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta \
    OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
    TEEC_EXPORT=${TEEC_EXPORT} \
    HOST_CROSS_COMPILE=${TARGET_PREFIX} \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    RUST_TARGET=armv7-unknown-linux-gnueabihf \
    LIBGCC_LOCATE_CFLAGS='--sysroot=${STAGING_DIR_HOST}' \
    V=1 \
    "

EXTRA_OEMAKE_imx8mqevk = " \
    TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta_arm64 \
    OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
    TEEC_EXPORT=${TEEC_EXPORT} \
    HOST_CROSS_COMPILE=${TARGET_PREFIX} \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    RUST_TARGET=aarch64-unknown-linux-gnu \
    LIBGCC_LOCATE_CFLAGS='--sysroot=${STAGING_DIR_HOST}' \
    V=1 \
    "

EXTRA_OEMAKE_flex-imx8mm = " \
    TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta_arm64 \
    OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
    TEEC_EXPORT=${TEEC_EXPORT} \
    HOST_CROSS_COMPILE=${TARGET_PREFIX} \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    RUST_TARGET=aarch64-unknown-linux-gnu \
    LIBGCC_LOCATE_CFLAGS='--sysroot=${STAGING_DIR_HOST}' \
    V=1 \
    "

EXTRA_OEMAKE_pico-imx8mq = " \
    TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta_arm64 \
    OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
    TEEC_EXPORT=${TEEC_EXPORT} \
    HOST_CROSS_COMPILE=${TARGET_PREFIX} \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    RUST_TARGET=aarch64-unknown-linux-gnu \
    LIBGCC_LOCATE_CFLAGS='--sysroot=${STAGING_DIR_HOST}' \
    V=1 \
    "

EXTRA_OEMAKE_qemu-optee64 = " \
    TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta \
    OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
    TEEC_EXPORT=${TEEC_EXPORT} \
    HOST_CROSS_COMPILE=${TARGET_PREFIX} \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    RUST_TARGET=aarch64-unknown-linux-gnu \
    LIBGCC_LOCATE_CFLAGS='--sysroot=${STAGING_DIR_HOST}' \
    V=1 \
    "

do_compile() {
    export PATH=${HOME}/.cargo/bin:$PATH
    oe_runmake
}

do_install () {
    mkdir -p ${D}${nonarch_base_libdir}/optee_armtz
    mkdir -p ${D}${bindir}
    install -m 755 ${S}/host/hello_rustee ${D}${bindir}/
    install -m 444 ${S}/ta/*.ta ${D}${nonarch_base_libdir}/optee_armtz
}

FILES_${PN} += "${nonarch_base_libdir}/optee_armtz/"

INSANE_SKIP_${PN} = "ldflags"
INSANE_SKIP_${PN}-dev = "ldflags"

# Imports machine specific configs from staging to build
PACKAGE_ARCH = "${MACHINE_ARCH}"
