package net.graysenko.com.ForumDQC.Services;

import net.graysenko.com.ForumDQC.Models.Media;
import net.graysenko.com.ForumDQC.Repositories.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MediaService {
    private final MediaRepository mediaRepository;

    @Autowired
    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public List<Media> findByPostId(Long id) {
        return mediaRepository.findByPost_Id(id);
    }

    public Media findByFilename(String filename) {
        return mediaRepository.findByFilename(filename);
    }

    public Media findById(Long id) {
        return mediaRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Media media) {
        mediaRepository.save(media);
    }
}
