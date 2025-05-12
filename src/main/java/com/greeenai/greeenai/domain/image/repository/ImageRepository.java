package com.greeenai.greeenai.domain.image.repository;

import com.greeenai.greeenai.domain.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {}
