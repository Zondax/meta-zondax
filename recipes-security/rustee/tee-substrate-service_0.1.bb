SUMMARY = "TEE substrate service"
HOMEPAGE = "https://github.com/Zondax/tee-substrate-service"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "gitsm://github.com/Zondax/tee-substrate-service.git;branch=master;protocol=https \
           file://tee-substrate-service.service \
           file://tee-substrate-service \
"
SRCREV = "master"

PROJ_NAME = "tee-substrate-service"

require rustee.inc

inherit systemd update-rc.d

do_install:append() {
    mkdir -p ${D}${sbindir}
    install -m 755 ${S}/framework/host/src/${PROJ_NAME} ${D}${sbindir}/

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        sed -i -e s:@sysconfdir@:${sysconfdir}:g \
               -e s:@sbindir@:${sbindir}:g \
                  ${WORKDIR}/tee-substrate-service.service

        install -D -p -m0644 ${WORKDIR}/tee-substrate-service.service ${D}${systemd_system_unitdir}/tee-substrate-service.service
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
        sed -i -e s:@sysconfdir@:${sysconfdir}:g \
               -e s:@sbindir@:${sbindir}:g \
                  ${WORKDIR}/tee-substrate-service

        install -D -p -m0755 ${WORKDIR}/tee-substrate-service ${D}${sysconfdir}/init.d/tee-substrate-service
    fi
}

SYSTEMD_SERVICE:${PN} = "tee-substrate-service.service"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "tee-substrate-service"
INITSCRIPT_PARAMS:${PN} = "start 15 1 2 3 4 5 . stop 85 0 6 ."
