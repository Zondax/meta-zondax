SUMMARY = "OP-TEE hello_rustee"
HOMEPAGE = "https://github.com/Zondax/hello-rustee"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "optee-client optee-os python3-pycrypto-native"

inherit python3native

SRC_URI = "git:///home/zondax/hello-rustee.git;protocol=file;rev=master"
# or use SRC_URI = "file:///home/zondax/hello-rustee.git/src"

S = "${WORKDIR}/git/src"

OPTEE_CLIENT_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TEEC_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TA_DEV_KIT_DIR = "${STAGING_INCDIR}/optee/export-user_ta"
CARGO_BIN = "/home/zondax/.cargo/bin"

EXTRA_OEMAKE = " TA_DEV_KIT_DIR=${TA_DEV_KIT_DIR} \
                 OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
                 TEEC_EXPORT=${TEEC_EXPORT} \
                 HOST_CROSS_COMPILE=${TARGET_PREFIX} \
                 TA_CROSS_COMPILE=${TARGET_PREFIX} \
                 CARGO_BIN=${CARGO_BIN} \
                 V=1 \
               "

do_compile() {
    oe_runmake
}

do_install () {
    mkdir -p ${D}${nonarch_base_libdir}/optee_armtz
    mkdir -p ${D}${bindir}
    install -m 755 ${S}/host/hello_rustee ${D}${bindir}/
    install -m 444 ${S}/ta/*.ta ${D}${nonarch_base_libdir}/optee_armtz
}

FILES_${PN} += "${nonarch_base_libdir}/optee_armtz/"
INSANE_SKIP_${PN}-dev = "ldflags"

# Imports machine specific configs from staging to build
PACKAGE_ARCH = "${MACHINE_ARCH}"
