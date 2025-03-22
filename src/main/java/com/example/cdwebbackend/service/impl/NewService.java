//package com.example.cdwebbackend.service.impl;
//
//import com.laptrinhjavaweb.converter.NewConverter;
//import com.laptrinhjavaweb.dto.NewDTO;
//import com.laptrinhjavaweb.entity.CategoryEntity;
//import com.laptrinhjavaweb.entity.NewEntity;
//import com.laptrinhjavaweb.repository.CategoryRepository;
//import com.laptrinhjavaweb.repository.NewRepository;
//import com.laptrinhjavaweb.service.INewService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class NewService implements INewService {
//
//    @Autowired
//    private NewRepository newRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Autowired
//    private NewConverter newConverter;
//    @Override
//    public NewDTO save(NewDTO newDTO) {
//        NewEntity newEntity = new NewEntity();
//        if(newDTO.getId()!=null){
//            NewEntity oldNew = newRepository.findOneById(newDTO.getId());
//             newEntity = newConverter.toEntity(newDTO, oldNew);
//        }else{
//            newEntity = newConverter.toEntity(newDTO);
//        }
//
//        CategoryEntity categoryEntity = categoryRepository.findOneByCode(newDTO.getCategoryCode());
//        newEntity.setCategory(categoryEntity);
//        newEntity = newRepository.save(newEntity);
//        newConverter = new NewConverter();
//        return newConverter.toDTO(newEntity);
//    }
//
//    @Override
//    public void delete(long[] ids) {
//
//        for (long item: ids){
//            newRepository.deleteById(item);
//        }
//    }
//
//    @Override
//    public List<NewDTO> findAll(Pageable pageable) {
//        List<NewDTO>result = new ArrayList<>();
//        List<NewEntity>entities = newRepository.findAll(pageable).getContent();
//        for(NewEntity item: entities){
//            NewDTO newDTO =newConverter.toDTO(item);
//            result.add(newDTO);
//        }
//        return result;
//    }
//
//    @Override
//    public int totalItem() {
//        return (int) newRepository.count();
//    }
//
////    @Override
////    public NewDTO update(NewDTO newDTO) {
////        NewEntity oldNew = newRepository.findOneById(newDTO.getId());
////        NewEntity newEntity = newConverter.toEntity(newDTO, oldNew);
////        CategoryEntity categoryEntity = categoryRepository.findOneByCode(newDTO.getCategoryCode());
////
////        newEntity.setCategory(categoryEntity);
////        newEntity=newRepository.save(newEntity);
////        return newConverter.toDTO(newEntity);
////    }
//}
