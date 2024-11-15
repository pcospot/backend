package net.yellowstrawberry.pcospot.service;

import com.github.f4b6a3.tsid.Tsid;
import net.yellowstrawberry.pcospot.db.repository.FileRepository;
import net.yellowstrawberry.pcospot.object.file.FileEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class StoreService {

    private final FileRepository fileRepository;
    private final Path root = Path.of("./uploads/");

    public StoreService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public long store(MultipartFile file) {
        try {
            FileEntity f = new FileEntity(Tsid.fast().toLong(), file.getOriginalFilename(), file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
            Files.copy(file.getInputStream(), this.root.resolve(f.getId()+"."+f.getType()));
            fileRepository.save(f);
            return f.getId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ByteArrayResource load(Long id) {
        Optional<FileEntity> e = fileRepository.findById(id);
        if(e.isPresent()) {
            try {
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(this.root.resolve(e.get().getId()+"."+e.get().getType())));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return null;
    }

    public void delete(Long id) {
        Optional<FileEntity> e = fileRepository.findById(id);
        if(e.isPresent()) {
            try {
                Files.delete(this.root.resolve(e.get().getId()+"."+e.get().getType()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            fileRepository.delete(e.get());
        }
    }
}
