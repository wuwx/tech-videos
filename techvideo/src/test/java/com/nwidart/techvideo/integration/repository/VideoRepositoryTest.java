package com.nwidart.techvideo.integration.repository;

import com.nwidart.techvideo.entity.Session;
import com.nwidart.techvideo.entity.Video;
import com.nwidart.techvideo.repository.VideoRepository;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VideoRepositoryTest {

  @Autowired
  private VideoRepository repository;
  @Autowired
  private TestEntityManager testEntityManager;

  @Before
  public void setUp() throws Exception {
    testEntityManager.persistAndFlush(new Video("Title One", "url"));
  }

  @Test
  public void findAllReturnsAllVideos() {
    List<Video> videos = repository.findAll();

    Assert.assertEquals(1, videos.size());
    Assert.assertEquals("Title One", videos.get(0).getTitle());
  }

  @Test
  public void findAvailableVideosReturnsCorrectVideos() {
    Session session = testEntityManager.persistAndFlush(new Session(OffsetDateTime.now()));
    testEntityManager.persistAndFlush(new Video("Title One", "url", session));

    List<Video> videos = repository.findAllVideosToVoteOn();

    Assert.assertEquals(1, videos.size());
  }
}
