package com.sparta.finalproject.domain.classroom.service;

import com.sparta.finalproject.domain.child.dto.ChildResponseDto;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.domain.classroom.dto.*;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.domain.kindergarten.entity.Kindergarten;
import com.sparta.finalproject.domain.kindergarten.repository.KindergartenRepository;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.domain.user.repository.UserRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.ClassroomException;
import com.sparta.finalproject.global.response.exceptionType.KindergartenException;
import com.sparta.finalproject.global.response.exceptionType.UserException;
import com.sparta.finalproject.global.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.finalproject.global.response.CustomStatusCode.*;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final ChildRepository childRepository;
    private final KindergartenRepository kindergartenRepository;
    private static final int CHILD_SIZE = 14;
    private final UserRepository userRepository;

    @Transactional
    public GlobalResponseDto addClassroom(Long kindergartenId, String name, User user){
        UserValidator.validatePrincipal(user);
        if(name.isEmpty()){
            throw new ClassroomException(CLASSROOM_NAME_EMPTY);
        }
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId).orElseThrow(
                () -> new KindergartenException(KINDERGARTEN_NOT_FOUND)
        );
        List<Classroom> classroomList = classroomRepository.findByKindergartenId(kindergartenId);
        for(Classroom classroom : classroomList){
            if(name.equals(classroom.getName())){
                throw new ClassroomException(CLASSROOM_NAME_DUPLICATE);
            }
        }
        Classroom classroom = Classroom.of(name,kindergarten);
        classroomRepository.save(classroom);
        return GlobalResponseDto.of(ADD_CLASSROOM_SUCCESS, ClassroomAddResponseDto.of(classroom.getId(), name));
    }

    @Transactional
    public GlobalResponseDto modifyClassroom(Long kindergartenId, Long classroomId, String name, User user){
        UserValidator.validatePrincipal(user);
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId).orElseThrow(
                () -> new KindergartenException(KINDERGARTEN_NOT_FOUND)
        );
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ClassroomException(CLASSROOM_NOT_FOUND)
        );
        if(name.isEmpty()){
            throw new ClassroomException(CLASSROOM_NAME_EMPTY);
        }
        List<Classroom> classroomList = classroomRepository.findByKindergartenId(kindergartenId);
        classroomList.remove(classroom);
        for(Classroom found : classroomList){
            if(name.equals(found.getName())){
                throw new ClassroomException(CLASSROOM_NAME_DUPLICATE);
            }
        }
        classroom.update(name);
        return GlobalResponseDto.of(MODIFY_CLASSROOM_SUCCESS, ClassroomModifyResponseDto.of(classroomId,name));
    }

    @Transactional
    public GlobalResponseDto deleteClassroom(Long kindergartenId, Long classroomId){
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId).orElseThrow(
                () -> new KindergartenException(KINDERGARTEN_NOT_FOUND)
        );
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                ()->new ClassroomException(CLASSROOM_NOT_EXIST)
        );
        classroomRepository.deleteById(classroomId);
        return GlobalResponseDto.from(REMOVE_CLASSROOM_SUCCESS);
    }

    @Transactional
    public GlobalResponseDto findClassroomList(Long kindergartenId, User user){
        UserValidator.validatePrincipal(user);
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId).orElseThrow(
                () -> new KindergartenException(KINDERGARTEN_NOT_FOUND)
        );
        List<Classroom> classroomList = classroomRepository.findByKindergartenId(kindergartenId);
        List<ClassroomInfoDto> classList = new ArrayList<>();
        for(Classroom classroom : classroomList){
            classList.add(ClassroomInfoDto.of(classroom.getId(), classroom.getName()));
        }
        return GlobalResponseDto.of(CLASSROOM_LIST_SUCCESS, ClassroomInfoListResponseDto.from(classList));
    }

    @Transactional(readOnly = true)
    public GlobalResponseDto findClassroom(Long kindergartenId, Long classroomId, int page, User user) {
        UserValidator.validateParentAndTeacherAndPrincipal(user);

        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId).orElseThrow(
                () -> new KindergartenException(KINDERGARTEN_NOT_FOUND)
        );
        Classroom classroom;
        if(classroomId == -1){
            classroom = classroomRepository.findClassroomWithLowestId();
            if(classroom==null){
                return GlobalResponseDto.from(CLASSROOM_LIST_SUCCESS);
            }
            classroomId = classroom.getId();
        }
        else {
            classroom = classroomRepository.findById(classroomId).orElseThrow(
                    () -> new ClassroomException(CLASSROOM_NOT_FOUND)
            );
            classroomId = classroom.getId();
        }
        List<ClassroomInfoDto> everyClass = new ArrayList<>();
        List<Classroom> classroomList = classroomRepository.findAllByOrderByIdAscAndKindergartenId(kindergartenId);
        for(Classroom found : classroomList){
            everyClass.add(ClassroomInfoDto.of(found.getId(), found.getName()));
        }

        Pageable pageable = PageRequest.of(page, CHILD_SIZE, Sort.by(Sort.Direction.ASC, "id"));
        Page<Child> children = childRepository.findAllByClassroomId(classroomId, pageable);
        Long childrenCount = childRepository.countAllByClassroomId(classroomId);
        List<ChildResponseDto> responseDtoList = children.stream().map(ChildResponseDto::of).collect(Collectors.toList());
        ClassroomTeacherResponseDto classroomTeacher;
        if(classroom.getClassroomTeacher() != null){
            classroomTeacher = new ClassroomTeacherResponseDto(classroom.getClassroomTeacher());
            return GlobalResponseDto.of(CustomStatusCode.FIND_CLASSROOM_SUCCESS,
                    ClassroomResponseDto.of(classroomId, responseDtoList, childrenCount, classroomTeacher, everyClass));
        }
        return GlobalResponseDto.of(CustomStatusCode.FIND_CLASSROOM_SUCCESS,
                ClassroomResponseDto.of(classroomId, responseDtoList, childrenCount, everyClass));
    }

    @Transactional
    public GlobalResponseDto modifyClassroomTeacher(Long kindergartenId, Long classroomId, Long teacherId, User user) {
        UserValidator.validatePrincipal(user);
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId).orElseThrow(
                () -> new KindergartenException(KINDERGARTEN_NOT_FOUND)
        );
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ClassroomException(CLASSROOM_NOT_FOUND)
        );
        User classroomTeacher = userRepository.findById(teacherId).orElseThrow(
                () -> new UserException(CustomStatusCode.TEACHER_NOT_FOUND)
        );
        classroom.update(classroomTeacher);
        return GlobalResponseDto.of(CustomStatusCode.MODIFY_CLASSROOM_TEACHER_SUCCESS, null);
    }
}
