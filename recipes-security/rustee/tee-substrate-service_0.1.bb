SUMMARY = "TEE substrate service"
HOMEPAGE = "https://github.com/Zondax/tee-substrate-service"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "gitsm://github.com/Zondax/tee-substrate-service.git;branch=master;protocol=https"
SRCREV = "master"

PROJ_NAME = "tee-substrate-service"

require rustee.inc

do_install:append() {
    mkdir -p ${D}${bindir}
    install -m 755 ${S}/framework/host/src/${PROJ_NAME} ${D}${bindir}/
}
