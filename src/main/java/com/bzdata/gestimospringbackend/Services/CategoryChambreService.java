package com.bzdata.gestimospringbackend.Services;

import com.bzdata.gestimospringbackend.DTOs.CategoryChambreSaveOrUpdateDto;
import com.itextpdf.layout.element.List;

public interface CategoryChambreService extends AbstractService<CategoryChambreSaveOrUpdateDto>{
 CategoryChambreSaveOrUpdateDto saveOrUpdateCategoryChambre(CategoryChambreSaveOrUpdateDto dto);
}
