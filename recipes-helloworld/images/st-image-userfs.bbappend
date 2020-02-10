PACKAGE_INSTALL += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'optee', 'optee-helloworld', '', d)} \
    "
