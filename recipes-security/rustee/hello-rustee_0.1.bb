SUMMARY = "hello_rustee application"
HOMEPAGE = "https://github.com/Zondax/hello-rustee"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "gitsm://github.com/Zondax/hello-rustee.git;branch=master;protocol=https"
SRCREV = "master"

PROJ_NAME = "hello-rustee"

require rustee.inc

do_install:append() {
    mkdir -p ${D}${bindir}
    install -m 755 ${S}/framework/host/src/${PROJ_NAME} ${D}${bindir}/
}
