import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentService } from '../service/student.service';
import { Student } from '../model/student';

@Component({
  selector: 'app-student-form',
  templateUrl: './student-form.component.html',
  styleUrls: ['./student-form.component.css']
})
export class StudentFormComponent {

  student: Student;
  errorMessage = '';

  constructor(private route: ActivatedRoute, private router: Router, private studentService: StudentService) {
    this.student = new Student();
  }

  onSubmit() {
    this.errorMessage = '';
    this.studentService.save(this.student).subscribe({
      next: () => this.gotoStudentList(),
      error: () => this.errorMessage = 'Student konnte nicht gespeichert werden. Bitte Name und E-Mail prüfen.'
    });
  }

  gotoStudentList() {
    this.router.navigate(['/students']);
  }
}
