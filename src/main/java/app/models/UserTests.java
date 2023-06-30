package app.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Locale;

@Entity
public class UserTests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idt;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="subcategoryId", nullable=false)
    private SubCategories subCategory;

    @Column(length = 2047)
    private String answer1;

    @Column(length = 2047)
    private String answer2;

    @Column(length = 2047)
    private String answer3;

    @Column(length = 2047)
    private String answer4;

    @Column(length = 2047)
    private String answer5;

    @Column(length = 2047)
    private String summary;

    private LocalDateTime testtime;

    public LocalDateTime getTesttime() {
        return testtime;
    }

    public void setTesttime(LocalDateTime testtime) {
        this.testtime = testtime;
    }

    public Long getIdt() {
        return idt;
    }

    public void setIdt(Long idt) {
        this.idt = idt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SubCategories getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategories subCategory) {
        this.subCategory = subCategory;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getAnswer5() {
        return answer5;
    }

    public void setAnswer5(String answer5) {
        this.answer5 = answer5;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
