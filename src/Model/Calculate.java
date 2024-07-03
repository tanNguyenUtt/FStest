/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author 30120
 */
public class Calculate {
    private double tax;
    private double subtotal;

    public double getTax() {
        return calTax(subtotal);
    }

    public double getTotal(){
        return subtotal + getTax();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    private double calTax(double t){
        if(t >= 100000.0 && t <= 200000.0){
            tax = 0.5;
        }else if(t > 200000.0 && t <= 400000.0){
            tax = 1.0;
        }else if(t > 400000.0 && t <= 600000.0){
            tax = 2.0;
        }else if(t > 600000.0 && t <= 800000.0){
            tax = 3.0;
        }else if(t > 800000.0 && t <= 1000000.0){
            tax = 4.0;
        }else if(t > 1000000.0 && t <= 1500000.0){
            tax = 8.0;
        }else if(t > 1500000.0 && t <= 2000000.0){
            tax = 10000.0;
        }else if(t > 2000000.0 ){
            tax = 15.0;
        }
        return tax;
    }
}
