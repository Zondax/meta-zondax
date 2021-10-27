SUMMARY = "OP-TEE test package group"
DESCRIPTION = "Provide optee-test package"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/LGPL-2.0-or-later;md5=6d2d9952d88b50a51a5c73dc431d06c7"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "packagegroup-optee-test"

PROVIDES = "${PACKAGES}"

RDEPENDS:packagegroup-optee-test = "\
    optee-test \
"
