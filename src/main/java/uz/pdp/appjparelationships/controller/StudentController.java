package uz.pdp.appjparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appjparelationships.entity.Address;
import uz.pdp.appjparelationships.entity.Group;
import uz.pdp.appjparelationships.entity.Student;
import uz.pdp.appjparelationships.entity.Subject;
import uz.pdp.appjparelationships.payload.StudentDto;
import uz.pdp.appjparelationships.repository.AddressRepository;
import uz.pdp.appjparelationships.repository.GroupRepository;
import uz.pdp.appjparelationships.repository.StudentRepository;
import uz.pdp.appjparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    StudentRepository studentRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage;
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {
        //1-1=0     2-1=1    3-1=2    4-1=3
        //select * from student limit 10 offset (0*10)
        //select * from student limit 10 offset (1*10)
        //select * from student limit 10 offset (2*10)
        //select * from student limit 10 offset (3*10)
        Pageable pageable = PageRequest.of(page, 10);
        Page<Student> studentPage = studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
        return studentPage;
    }

    //3. FACULTY DEKANAT
    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStundentListForFaculty(@PathVariable Integer facultyId,@RequestParam int page){
        Pageable pageable=PageRequest.of(page,10);
        Page<Student> studentPageFaculty = studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
        return studentPageFaculty;
    }


    //4. GROUP OWNER
    @GetMapping("/forGroupOwner/{groupId}")
    public List<Student> getStudentList(@PathVariable Integer groupId){
        List<Student> allByGroupId = studentRepository.findAllByGroupId(groupId);
        return allByGroupId;
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id){
        studentRepository.deleteById(id);
        return "delete Student";
    }

    @PostMapping("/addStudent")
    public String addStudent(@RequestBody StudentDto studentDto){
        Student student=new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
        if (!optionalAddress.isPresent()){
            student.setAddress(new Address());
            return "address mavjud emas";
        }
        student.setAddress(optionalAddress.get());
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (!optionalGroup.isPresent()){
            return "group not found";
        }
        student.setGroup(optionalGroup.get());
        List<Subject> subjects = subjectRepository.findAll();
        student.setSubjects(subjects);

        studentRepository.save(student);

        return "add student";
    }

    @PutMapping("/{id}")
    public String editStudent(@PathVariable Integer id,@RequestBody StudentDto studentDto){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()){
            Student student = optionalStudent.get();
            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());
            Optional<Address> optionalAddress = addressRepository.findById(studentDto.getAddressId());
            if (!optionalAddress.isPresent()){
                student.setAddress(new Address());
                return "address mavjud emas";
            }
            student.setAddress(optionalAddress.get());
            Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
            if (!optionalGroup.isPresent()){
                return "group not found";
            }
            student.setGroup(optionalGroup.get());
            List<Subject> subjects = subjectRepository.findAll();
            student.setSubjects(subjects);

            studentRepository.save(student);

            return "edit student";
        }
        return "not found student";
    }

}
