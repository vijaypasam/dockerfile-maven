File imageIdFile = new File(basedir, "target/docker/image-id")
assert imageIdFile.isFile()

File repositoryFile = new File(basedir, "target/docker/repository")
assert repositoryFile.text == "test/build-into-tag\n"

File tagFile = new File(basedir, "target/docker/tag")
assert tagFile.text == "unstable\n"

File imageNameFile = new File(basedir, "target/docker/image-name")
assert imageNameFile.text == "test/build-into-tag:unstable\n"
