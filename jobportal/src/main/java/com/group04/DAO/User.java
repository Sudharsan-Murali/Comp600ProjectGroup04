package com.group04.DAO;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String dob;
    private String password;
    private int role;
    private String securityQuestion;
    private String securityAnswer;
    private String availability;
    private String company;
    private String preferences;
    private String jobRole;
    private String linkedInUrl;

    // Constructors
    public User() {}

    public User(String firstName, String lastName, String email, String mobile, String dob, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.dob = dob;
        this.password = password;
    }

    public User(int id, String firstName, String lastName, String email, String mobile, String dob, String password, int roleId, String securityQuestion, String securityAnswer) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.dob = dob;
        this.password = password;
        this.role = roleId;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    public User(String firstName, String lastName, String email, String mobile, String dob, String password, int roleId, String securityQuestion, String securityAnswer) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.dob = dob;
        this.password = password;
        this.role = roleId;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }

    // Getters and Setters (you can generate via IDE)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getRoleId() { return role; }
    public void setRole(int role) { this.role = role; }

    public String getSecurityQuestion() { return securityQuestion; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswer() { return securityAnswer; }
    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobRole() {
        return jobRole;
    }
    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getPreferences() {
        return preferences;
    }
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }
    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    public String getAvailability() {
        return availability;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
