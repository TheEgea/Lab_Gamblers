package com.tecnocampus.LS2.protube_back.web;

import com.tecnocampus.LS2.protube_back.application.auth.LoginService;
import com.tecnocampus.LS2.protube_back.domain.user.RawPassword;
import com.tecnocampus.LS2.protube_back.domain.user.Username;
import com.tecnocampus.LS2.protube_back.web.dto.request.LoginRequest;
import com.tecnocampus.LS2.protube_back.web.dto.response.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoCatalogPort videoCatalogPort;
    private final VideoMapper mapper;

    @GetMapping
    public ResponseEntity<List<VideoDTO>> listAll() {
        var dtos = videoCatalogPort.listAll().stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDTO> getById(@PathVariable String id) {
        return videoCatalogPort.findById(new VideoId(id))
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> create(@Valid @RequestBody CreateVideoRequest req, UriComponentsBuilder uri) {
        var video = mapper.fromCreateRequest(req);
        videoCatalogPort.save(video);
        var location = uri.path("/videos/{id}").buildAndExpand(video.getId().value()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoDTO> update(@PathVariable String id, @Valid @RequestBody UpdateVideoRequest req) {
        var maybe = videoCatalogPort.findById(new VideoId(id));
        if (maybe.isEmpty()) return ResponseEntity.notFound().build();
        var updated = mapper.applyUpdate(maybe.get(), req);
        // si el puerto soporta save como upsert o se añade update al puerto:
        videoCatalogPort.save(updated);
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        // opcional: comprobar existencia primero
        videoCatalogPort.delete(new VideoId(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/random")
    public ResponseEntity<VideoDTO> random() {
        return videoCatalogPort.getRandomVideo()
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
