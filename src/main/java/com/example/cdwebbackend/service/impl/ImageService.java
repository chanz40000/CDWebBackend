package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.dto.ImageDTO;
import com.example.cdwebbackend.repository.ImageRepository;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.service.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    InetAddress ip;

    {
        try {
            ip = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    String ipAddress = ip.toString();

    @Override
    public ImageDTO uploadImage(MultipartFile file, Long entityId, String entityType) throws IOException {
        return null;
    }
}
