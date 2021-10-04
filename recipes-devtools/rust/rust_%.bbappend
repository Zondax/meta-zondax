python do_configure_append() {
    import json

    try:
        import configparser
    except ImportError:
        import ConfigParser as configparser

    e = lambda s: json.dumps(s)

    config = configparser.RawConfigParser()

    with open("config.toml", "r") as f:
        next(f)
        config.read_file(f)

    config.set("rust", "channel", e("nightly"))

    with open("config.toml", "w") as f:
        f.write('changelog-seen = 2\n\n')
        config.write(f)
}
