SUMMARY = "OPTEE client"
HOMEPAGE = "https://github.com/OP-TEE/optee_client"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=69663ab153298557a59c67a60a743e5b"

inherit python3native systemd

PV = "3.14.0+git${SRCPV}"

SRC_URI = "git://github.com/OP-TEE/optee_client.git \
           file://tee-supplicant.service \
           file://optee \
"

SRCREV = "06e1b32f6a7028e039c625b07cfc25fda0c17d53"

S = "${WORKDIR}/git"

SYSTEMD_SERVICE_${PN} = "tee-supplicant.service"

do_install() {
    oe_runmake install

    install -D -p -m0755 ${S}/out/export/usr/sbin/tee-supplicant ${D}${bindir}/tee-supplicant

    install -D -p -m0644 ${S}/out/export/usr/lib/libteec.so.1.0.0 ${D}${libdir}/libteec.so.1.0.0
    ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so
    ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1
    ln -sf libteec.so.1.0.0 ${D}${libdir}/libteec.so.1.0

    cp -a ${S}/out/export/usr/include ${D}/usr/

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        sed -i -e s:/etc:${sysconfdir}:g \
           -e s:/usr/bin:${bindir}:g \
              ${WORKDIR}/tee-supplicant.service

        install -D -p -m0644 ${WORKDIR}/tee-supplicant.service ${D}${systemd_system_unitdir}/tee-supplicant.service
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
        install -D -p -m0755 ${WORKDIR}/optee ${D}/etc/init.d/optee
        install -d ${D}/etc/rcS.d
        ln -sf ../init.d/optee ${D}/etc/rcS.d/S30optee
    fi
}
