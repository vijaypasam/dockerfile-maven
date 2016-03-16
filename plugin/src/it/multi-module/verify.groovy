File imageIdFile = new File(basedir, "b/target/docker-info-deps/META-INF/docker/com.spotify.it/a/image-id")
assert imageIdFile.isFile()

File repositoryFile = new File(basedir, "b/target/docker-info-deps/META-INF/docker/com.spotify.it/a/repository")
assert repositoryFile.text == "test/multi-module-a\n"

File tagFile = new File(basedir, "b/target/docker-info-deps/META-INF/docker/com.spotify.it/a/tag")
assert tagFile.text == "unstable\n"

File imageNameFile = new File(basedir, "b/target/docker-info-deps/META-INF/docker/com.spotify.it/a/image-name")
assert imageNameFile.text == "test/multi-module-a:unstable\n"
