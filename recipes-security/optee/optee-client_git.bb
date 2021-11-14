SUMMARY = "OPTEE client"
HOMEPAGE = "https://github.com/OP-TEE/optee_client"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=69663ab153298557a59c67a60a743e5b"

inherit python3native systemd update-rc.d

PV = "3.14.0+git${SRCPV}"

SRC_URI = "git://github.com/OP-TEE/optee_client.git;protocol=https \
           file://tee-supplicant.service \
           file://tee-supplicant \
"

SRCREV = "06e1b32f6a7028e039c625b07cfc25fda0c17d53"

S = "${WORKDIR}/git"

do_install() {
    oe_runmake install

    install -D -p -m0755 ${S}/out/export/usr/sbin/tee-supplicant ${D}${sbindir}/tee-supplicant

    install -D -p -m0644 ${S}/out/export/usr/lib/libteec.so.1.0.0 ${D}${libdir}/libteec.so.1.0.0
    ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so
    ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1
    ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1.0

    cp -a ${S}/out/export/usr/include ${D}/usr/

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        sed -i -e s:@sysconfdir@:${sysconfdir}:g \
               -e s:@sbindir@:${sbindir}:g \
                  ${WORKDIR}/tee-supplicant.service

        install -D -p -m0644 ${WORKDIR}/tee-supplicant.service ${D}${systemd_system_unitdir}/tee-supplicant.service
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
        sed -i -e s:@sysconfdir@:${sysconfdir}:g \
               -e s:@sbindir@:${sbindir}:g \
                  ${WORKDIR}/tee-supplicant

        install -D -p -m0755 ${WORKDIR}/tee-supplicant ${D}${sysconfdir}/init.d/tee-supplicant
    fi
}

SYSTEMD_SERVICE:${PN} = "tee-supplicant.service"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "tee-supplicant"
INITSCRIPT_PARAMS:${PN} = "start 10 1 2 3 4 5 . stop 90 0 6 ."
