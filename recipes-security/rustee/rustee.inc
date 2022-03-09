DEPENDS = "optee-client virtual/optee-os python3-pycrypto-native python3-pycryptodomex-native libgcc"
DEPENDS += "dtc-native python3-pyelftools-native python3-cryptography-native coreutils-native"

inherit cargo
inherit deploy python3native


S = "${WORKDIR}/git"
B = "${S}"

CARGO_DISABLE_BITBAKE_VENDORING = "1"

OPTEE_CLIENT_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TEEC_EXPORT = "${STAGING_DIR_HOST}${prefix}"

PROJ_NAME ?= "rustee_app"

TA_DEV_KIT_DIR="${STAGING_INCDIR}/optee/export-user_ta"
TA_DEV_KIT_DIR:mx8="${STAGING_INCDIR}/optee/export-user_ta_arm64"

ENABLE_TA_SIGNING ?= "0"
TA_CUSTOM_PRIVKEY ?= ""
TA_CUSTOM_PUBKEY ?= ""

EXTRA_OEMAKE = " \
    PROJ_NAME=${PROJ_NAME} \
    TA_DEV_KIT_DIR=${TA_DEV_KIT_DIR} \
    OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
    TEEC_EXPORT=${TEEC_EXPORT} \
    HOST_CROSS_COMPILE=${TARGET_PREFIX} \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    RUST_TARGET=${TARGET_SYS} \
    LIBGCC_LOCATE_CFLAGS='--sysroot=${STAGING_DIR_HOST}' \
    TA_SIGN_KEY=${TA_CUSTOM_PRIVKEY} \
    TA_PUB_KEY=${TA_CUSTOM_PUBKEY} \
    SIGN_TA=${ENABLE_TA_SIGNING} \
    V=1 \
    "

do_configure:append() {
    ${PYTHON} ${S}/framework/ta/src/newuuid.py
}

do_compile() {
    export RUSTFLAGS="${RUSTFLAGS}"
    export RUST_TARGET_PATH="${RUST_TARGET_PATH}"
    oe_runmake
}

do_install() {
    mkdir -p ${D}${nonarch_base_libdir}/optee_armtz
    install -m 444 ${S}/framework/ta/src/*.ta ${D}${nonarch_base_libdir}/optee_armtz
}

FILES:${PN} += "${nonarch_base_libdir}/optee_armtz/"

INSANE_SKIP:${PN} = "ldflags"
INSANE_SKIP:${PN}-dev = "ldflags"

# Imports machine specific configs from staging to build
PACKAGE_ARCH = "${MACHINE_ARCH}"
