package com.cook.cancook.util;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ImageUrlResolver {

  private static final String IMGUR_SEARCH_BASE_URL = "https://imgur.com/search/score/all?q=";
  private static final String IMGUR_SCHEME = "https://";
  private static final String IMGUR_PLACEHOLDER_URL = "https://i.imgur.com/41YUNlrb.jpg";
  private static final Pattern IMGUR_IMAGE_PATTERN =
      Pattern.compile("i\\.imgur\\.com/[a-zA-Z0-9]+\\.(?:jpg|jpeg|png|gif|webp)");
  private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(3))
      .followRedirects(HttpClient.Redirect.NORMAL)
      .build();
  private static final Map<String, String> IMGUR_CACHE = new ConcurrentHashMap<>();

  private static final String UNSPLASH_HOST = "source.unsplash.com";
  private static final String DEFAULT_QUERY = "food";

  private ImageUrlResolver() {}

  public static String resolveImageUrl(String providedImage, String name) {
    if (hasText(providedImage)) {
      String trimmedImage = providedImage.trim();
      if (!isDeprecatedUnsplashUrl(trimmedImage)) {
        return trimmedImage;
      }
    }

    return buildDefaultImageUrl(name);
  }

  public static String buildDefaultImageUrl(String name) {
    String query = sanitizeNameForQuery(name);
    if (!hasText(query)) {
      query = DEFAULT_QUERY;
    }

    return IMGUR_CACHE.computeIfAbsent(query, ImageUrlResolver::resolveFromImgurSearch);
  }

  private static String sanitizeNameForQuery(String value) {
    if (!hasText(value)) {
      return "";
    }

    return value.trim().replaceAll("[^a-zA-Z0-9\\s]", " ").replaceAll("\\s+", " ");
  }

  private static boolean hasText(String value) {
    return value != null && !value.trim().isEmpty();
  }

  private static boolean isDeprecatedUnsplashUrl(String imageUrl) {
    return imageUrl.contains(UNSPLASH_HOST);
  }

  private static String resolveFromImgurSearch(String query) {
    try {
      String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8).replace("+", "%20");
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(IMGUR_SEARCH_BASE_URL + encodedQuery))
          .timeout(Duration.ofSeconds(5))
          .header("User-Agent", "Mozilla/5.0")
          .GET()
          .build();

      HttpResponse<String> response =
          HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

      if (response.statusCode() >= 200 && response.statusCode() < 300) {
        Matcher matcher = IMGUR_IMAGE_PATTERN.matcher(response.body());
        if (matcher.find()) {
          return IMGUR_SCHEME + matcher.group();
        }
      }
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    } catch (IOException ex) {
      return IMGUR_PLACEHOLDER_URL;
    } catch (RuntimeException ex) {
      return IMGUR_PLACEHOLDER_URL;
    }

    return IMGUR_PLACEHOLDER_URL;
  }
}