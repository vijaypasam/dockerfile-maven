File imageIdFile = new File(basedir, "target/docker/image-id")
assert imageIdFile.isFile()

File repositoryFile = new File(basedir, "target/docker/repository")
assert repositoryFile.text == "test/build-tag-version\n"

File tagFile = new File(basedir, "target/docker/tag")
assert tagFile.text == "1.2.3-SNAPSHOT\n"

File imageNameFile = new File(basedir, "target/docker/image-name")
assert imageNameFile.text == "test/build-tag-version:1.2.3-SNAPSHOT\n"
