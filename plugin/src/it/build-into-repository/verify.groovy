File imageIdFile = new File(basedir, "target/docker/image-id")
assert imageIdFile.isFile()

File repositoryFile = new File(basedir, "target/docker/repository")
assert repositoryFile.text == "test/build-into-repository\n"

File tagFile = new File(basedir, "target/docker/tag")
assert tagFile.text == "latest\n"

File imageNameFile = new File(basedir, "target/docker/image-name")
assert imageNameFile.text == "test/build-into-repository:latest\n"
