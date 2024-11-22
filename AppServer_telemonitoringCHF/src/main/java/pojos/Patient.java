/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojos;

import java.time.LocalDate;
import java.util.ArrayList;


// should implement Serializable to establish the conncection with the server
public class Patient {
    
    private Integer id; //el automatico de la db
    private final String dni;  
    private String password;
    private String name;
    private String surname;
    private String email;
    private final Gender gender;
    private Integer phoneNumber; 
    private final LocalDate dob;
    private Doctor doctor;
    private ArrayList <Episode> episodes;


    //constructor for registration

    public Patient(String dni, String password, String name, String surname, String email, 
            Gender gender, Integer phoneNumber, LocalDate dob) {
        this.dni = dni;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
	episodes = new ArrayList<>();
    }
    
    
    //constructor with everything
    public Patient(Integer id, String dni, String password, String name, String surname, 
            String email, Gender gender, Integer phoneNumber, LocalDate dob, 
            Doctor doctor) {
        this.id = id;
        this.dni = dni;
        this.password = password;        
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.doctor = doctor;
        episodes = new ArrayList<>();
    }    
   
    
    public Integer getId() {
	return id;
    }


    public void setId(Integer id) {
	this.id = id;
    }

    public String getDNI() {
        return dni;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }


    public void setSurname(String surname) {
        this.surname = surname;
    }
  
    
    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }
    

    public Gender getGender() {
        return gender;
    }

    
    public Integer getPhoneNumber() {
            return phoneNumber;
    }


    public void setPhoneNumber(Integer phoneNumber) {
            this.phoneNumber = phoneNumber;
    }
    
    public LocalDate getDob() {
            return dob;
    }
    
    public Doctor getDoctor() {
            return doctor;
    }

    public ArrayList<Episode> getEpisodes() {
            return episodes;
    }

    public void setEpisodes(ArrayList<Episode> episodes) {
            this.episodes = episodes;
    }

    
    @Override
    public String toString() {
        return "Patient{" + "dni=" + dni + ", name=" + name + ", surname=" + surname + ", email=" + email + ", gender=" + gender + ", phoneNumber=" + phoneNumber + ", dob=" + dob + ", doctor=" + doctor + ", episodes=" + episodes + '}';
    }

         
    public enum Gender {
    MALE,
    FEMALE
}
}
