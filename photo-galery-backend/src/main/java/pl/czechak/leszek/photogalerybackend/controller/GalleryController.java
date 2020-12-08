package pl.czechak.leszek.photogalerybackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.czechak.leszek.photogalerybackend.dto.AddGalleryRequest;
import pl.czechak.leszek.photogalerybackend.model.gallery.GalleryEntity;
import pl.czechak.leszek.photogalerybackend.service.GalleryService;

import java.util.List;

@RestController
@RequestMapping("/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @PostMapping("/create")
    public ResponseEntity<GalleryEntity> createGallery(@RequestBody AddGalleryRequest galleryRequest) {
        return ResponseEntity.ok(galleryService.createGallery(galleryRequest));
    }

    @GetMapping("/{galleryId}")
    public ResponseEntity<GalleryEntity> getGalleryById(@PathVariable long galleryId) {
        GalleryEntity gallery = galleryService.getGalleryById(galleryId);
        return ResponseEntity.ok(gallery);
    }

}
