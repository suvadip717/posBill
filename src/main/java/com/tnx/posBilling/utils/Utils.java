package com.tnx.posBilling.utils;

import java.util.Collections;
import java.util.List;

import com.tnx.posBilling.dto.CategoryDTO;
import com.tnx.posBilling.dto.CategoryRootDTO;
import com.tnx.posBilling.dto.CompanyDTO;
import com.tnx.posBilling.dto.CompanySettingDTO;
import com.tnx.posBilling.dto.ProductDTO;
import com.tnx.posBilling.dto.SettingDTO;
import com.tnx.posBilling.model.ApplicationSetting;
import com.tnx.posBilling.model.Category;
import com.tnx.posBilling.model.Company;
import com.tnx.posBilling.model.Product;

public class Utils {
    public static ProductDTO mapProductdtoToProduct(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setBarCode(product.getBarCode());
        productDTO.setProductCode(product.getProductCode());
        productDTO.setProductLabel(product.getProductLabel());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setUnitPrice(product.getUnitPrice());
        productDTO.setMrp(product.getMrp());
        productDTO.setDiscountAmount(product.getDiscountAmount());
        productDTO.setDiscountPercentage(product.getDiscountPercentage());
        productDTO.setUnit(product.getUnit());
        productDTO.setSkuCode(product.getSkuCode());
        productDTO.setTaxPercentage(product.getTaxPercentage());
        productDTO.setStockQuantity(product.getStockQuantity());
        productDTO.setCategory(product.getCategory());
        productDTO.setCreatedAt(product.getCreatedAt());
        productDTO.setUpdatedAt(product.getUpdatedAt());
        return productDTO;
    }

    public static CategoryDTO mapCategoryDtoToCategory(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setImageUrl(category.getImageUrl());
        categoryDTO.setCategoryLabel(category.getCategoryLabel());
        categoryDTO.setProducts(category.getProducts());
        categoryDTO.setSubCategories(category.getSubCategories());
        categoryDTO.setParentCategory(category.getParentCategory());
        categoryDTO.setCreatedAt(category.getCreatedAt());
        categoryDTO.setUpdatedAt(category.getUpdatedAt());
        return categoryDTO;
    }

    public static CategoryRootDTO mapCategoryRootDtoToCategory(Category category) {
        CategoryRootDTO categoryDTO = new CategoryRootDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setImageUrl(category.getImageUrl());
        categoryDTO.setCategoryLabel(category.getCategoryLabel());
        // categoryDTO.setProducts(category.getProducts());
        categoryDTO.setSubCategories(Collections.emptyList());
        // categoryDTO.setParentCategory(category.getParentCategory());
        categoryDTO.setCreatedAt(category.getCreatedAt());
        categoryDTO.setUpdatedAt(category.getUpdatedAt());
        return categoryDTO;
    }

    public static CompanyDTO mapCompanyDTOtoCompany(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(company.getId());
        companyDTO.setName(company.getName());
        companyDTO.setEmail(company.getEmail());
        companyDTO.setPhone(company.getPhone());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setCreatedAt(company.getCreatedAt());
        companyDTO.setUpdatedAt(company.getUpdatedAt());
        return companyDTO;
    }

    public static SettingDTO mapSettingDTOtoApplicationSetting(ApplicationSetting setting, String currentValue) {
        SettingDTO settingDTO = new SettingDTO();
        settingDTO.setId(setting.getId());
        settingDTO.setSettingKey(setting.getSettingKey());
        settingDTO.setDescription(setting.getDescription());
        settingDTO.setSettingType(setting.getSettingType().name());
        settingDTO.setOptions(setting.getOptions());
        settingDTO.setDefaultValue(setting.getDefaultValue());
        settingDTO.setCurrentValue(currentValue);
        return settingDTO;
    }

    public static CompanySettingDTO mapCompanySettingDTOtoCompanySetting(CompanyDTO company,
            List<SettingDTO> settings) {
        CompanySettingDTO companySettingDTO = new CompanySettingDTO();
        companySettingDTO.setCompany(company);
        companySettingDTO.setSettings(settings);
        return companySettingDTO;
    }
}
