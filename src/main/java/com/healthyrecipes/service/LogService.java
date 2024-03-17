package com.healthyrecipes.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author:86198
 * @DATE:2024/3/17 18:26
 * @DESCRIPTION: Log Service
 * @VERSION:1.0
 */
public interface LogService {

    void upImages(MultipartFile[] images,Integer userid) ;
}
