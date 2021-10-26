SUMMARY = "hello_rustee application"
HOMEPAGE = "https://github.com/Zondax/hello-rustee"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "gitsm://github.com/Zondax/hello-rustee.git;branch=rustee_app"
SRCREV = "rustee_app"

PROJ_NAME = "hello-rustee"

require rustee.inc

do_install_append() {
    mkdir -p ${D}${bindir}
    install -m 755 ${S}/framework/host/src/${PROJ_NAME} ${D}${bindir}/
}
