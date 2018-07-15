package com.nwidart.techvideo.service;

import com.google.api.services.youtube.model.SearchListResponse;
import com.nwidart.techvideo.entity.Video;
import com.nwidart.techvideo.http.requests.CreateVideoRequest;
import com.nwidart.techvideo.repository.VideoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VideoService {

  private final VideoRepository videoRepository;
  private final YouTubeVideoInfoFetcher youTubeVideoInfoFetcher;
  private final YouTubeUrlParser urlParser;

  public VideoService(VideoRepository videoRepository, YouTubeVideoInfoFetcher youTubeVideoInfoFetcher,
      YouTubeUrlParser urlParser) {
    this.videoRepository = videoRepository;
    this.youTubeVideoInfoFetcher = youTubeVideoInfoFetcher;
    this.urlParser = urlParser;
  }

  public List<Video> all() {
    return videoRepository.findAll();
  }

  public Video create(CreateVideoRequest request) throws Exception {
    Video video = request.toModel();
    final String videoId = urlParser.getVideoId(video.getUrl());
    final SearchListResponse videoListResponse = youTubeVideoInfoFetcher.fetch(videoId);
    video.setTitle(videoListResponse.getItems().get(0).getSnippet().getTitle());

    return this.videoRepository.save(video);
  }
}
