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

    // Additional fields based on your schema.
    private String company;
    private String jobRole;
    private String preferences;
    private String linkedInUrl;
    private String availability;

    private byte[] profilePic; // to store binary image data
    private String resumePath; // to store the resume file path

    // Constructors
    public User() {
    }

    public User(String firstName, String lastName, String email, String mobile, String dob, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.dob = dob;
        this.password = password;
    }

    // Basic constructor with role and security info (can be used for registration)
    public User(int id, String firstName, String lastName, String email, String mobile, String dob,
            String password, int roleId, String securityQuestion, String securityAnswer) {
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

    // Optional: You can add another constructor that accepts the extra fields as
    // well.
    public User(String firstName, String lastName, String email, String mobile, String dob, String password,
            int roleId, String securityQuestion, String securityAnswer,
            String company, String jobRole, String preferences, String linkedInUrl, String availability) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobile = mobile;
        this.dob = dob;
        this.password = password;
        this.role = roleId;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.company = company;
        this.jobRole = jobRole;
        this.preferences = preferences;
        this.linkedInUrl = linkedInUrl;
        this.availability = availability;
    }

    // Getters and Setters for existing fields.
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    // Getters and Setters for the additional fields.
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

    // Getters and setters for the new fields:

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }
}
