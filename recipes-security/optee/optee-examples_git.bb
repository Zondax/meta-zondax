SUMMARY = "OP-TEE examples"
HOMEPAGE = "https://github.com/linaro-swg/optee_examples"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=cd95ab417e23b94f381dafc453d70c30"

DEPENDS = "optee-client virtual/optee-os python3-pycryptodomex-native python3-pycrypto-native"

inherit python3native

PV = "3.14.0+git${SRCPV}"

SRC_URI = "git://github.com/linaro-swg/optee_examples.git;protocol=https \
           file://0001-plugins-Honour-default-cross-compiler-environment-se.patch \
           file://0002-Makefile-Enable-plugins-installation-in-rootfs.patch \
           file://0003-plugins-remove-plugin-framework-examples-for-now.patch \
           "

SRCREV = "e9c870525af8f7e7fccf575a0ca5394ce55adcec"

S = "${WORKDIR}/git"

OPTEE_CLIENT_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TEEC_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TA_DEV_KIT_DIR = "${STAGING_INCDIR}/optee/export-user_ta"
TA_DEV_KIT_DIR:mx8 = "${STAGING_INCDIR}/optee/export-user_ta_arm64"

EXTRA_OEMAKE = " TA_DEV_KIT_DIR=${TA_DEV_KIT_DIR} \
                 OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
                 TEEC_EXPORT=${TEEC_EXPORT} \
                 HOST_CROSS_COMPILE=${TARGET_PREFIX} \
                 TA_CROSS_COMPILE=${TARGET_PREFIX} \
                 V=1 \
                 LIBGCC_LOCATE_CFLAGS='--sysroot=${STAGING_DIR_HOST}' \
               "

do_compile() {
    oe_runmake
}

do_install () {
    mkdir -p ${D}${nonarch_base_libdir}/optee_armtz
    mkdir -p ${D}${bindir}
    install -D -p -m0755 ${S}/out/ca/* ${D}${bindir}
    install -D -p -m0444 ${S}/out/ta/* ${D}${nonarch_base_libdir}/optee_armtz
}

# Avoid QA Issue: No GNU_HASH in the elf binary
INSANE_SKIP:${PN} += "ldflags"

FILES:${PN} += "${nonarch_base_libdir}/optee_armtz/ \
               "

# Imports machine specific configs from staging to build
PACKAGE_ARCH = "${MACHINE_ARCH}"
