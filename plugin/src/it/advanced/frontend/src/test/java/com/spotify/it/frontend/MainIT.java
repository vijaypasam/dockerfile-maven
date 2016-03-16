package com.spotify.it.frontend;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.common.net.HostAndPort;

import com.spotify.helios.testing.TemporaryJob;
import com.spotify.helios.testing.TemporaryJobs;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.describedAs;
import static org.hamcrest.Matchers.is;

public class MainIT {

  @Rule
  public final TemporaryJobs temporaryJobs = TemporaryJobs.create();

  private WebTarget frontend;
  private WebTarget backend;

  @Before
  public void setUp() throws Exception {
    TemporaryJob backendJob = temporaryJobs.job()
        .image(Resources.toString(
            Resources.getResource("META-INF/docker/com.spotify.it/backend/image-name"),
            Charsets.UTF_8).trim())
        .port("http", 1337)
        .deploy();
    HostAndPort backendAddress = backendJob.address("http");
    URI backendUri = httpUri(backendAddress);

    TemporaryJob frontendJob = temporaryJobs.job()
        .image(Files.readFirstLine(new File("target/docker/image-name"),
                                   Charsets.UTF_8))
        .command(backendUri.toString())
        .port("http", 1338)
        .deploy();
    HostAndPort frontendAddress = frontendJob.address("http");
    URI frontendUri = httpUri(frontendAddress);

    Client client = ClientBuilder.newClient();
    frontend = client.target(frontendUri);
    backend = client.target(backendUri);
  }

  private URI httpUri(HostAndPort frontendAddress) {
    return URI.create(
        String.format("http://%s:%d", frontendAddress.getHostText(), frontendAddress.getPort()));
  }

  @Test
  public void testVersion() throws Exception {
    String version = backend.path("/api/version").request(MediaType.TEXT_PLAIN).get(String.class);
    String homepage = frontend.path("/").request(MediaType.TEXT_HTML).get(String.class);

    assertThat(homepage, containsString("Backend version: " + version));
  }

  @Test
  public void testLowercase() throws Exception {
    String homepage = frontend.path("/").request(MediaType.TEXT_HTML).get(String.class);
    Pattern pattern = Pattern.compile("Lower case of ([^ <]+) is according to backend ([^ <]+)");

    Matcher matcher = pattern.matcher(homepage);
    assertThat(matcher.find(), describedAs("the pattern was found", is(true)));
    assertThat(matcher.group(2), is(matcher.group(1).toLowerCase()));
  }
}
