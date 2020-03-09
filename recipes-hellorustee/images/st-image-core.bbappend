IMAGE_INSTALL_append += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'optee', 'optee-hellorustee', '', d)} \
    "
