SUMMARY = "OP-TEE hello_world"
HOMEPAGE = "https://github.com/linaro-swg/hello_world"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=9486d0af6cc425e1ae48e452e698099e"

DEPENDS = "optee-client optee-os python3-pycrypto-native"

inherit python3native

SRC_URI = "git://github.com/igoropaniuk/hello_world.git"
SRCREV = "91def2996fc4ccdfa422f101d19752126b6680ff"

S = "${WORKDIR}/git"

OPTEE_CLIENT_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TEEC_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TA_DEV_KIT_DIR = "${STAGING_INCDIR}/optee/export-user_ta"

EXTRA_OEMAKE = " TA_DEV_KIT_DIR=${TA_DEV_KIT_DIR} \
                 OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
                 TEEC_EXPORT=${TEEC_EXPORT} \
                 HOST_CROSS_COMPILE=${TARGET_PREFIX} \
                 TA_CROSS_COMPILE=${TARGET_PREFIX} \
                 V=1 \
               "

do_compile() {
    oe_runmake
}

do_install () {
    mkdir -p ${D}${nonarch_base_libdir}/optee_armtz
    mkdir -p ${D}${bindir}
    install -m 755 ${S}/host/optee_example_hello_world ${D}${bindir}/
    install -m 444 ${S}/ta/*.ta ${D}${nonarch_base_libdir}/optee_armtz
}

FILES_${PN} += "${nonarch_base_libdir}/optee_armtz/"
INSANE_SKIP_${PN}-dev = "ldflags"

# Imports machine specific configs from staging to build
PACKAGE_ARCH = "${MACHINE_ARCH}"
