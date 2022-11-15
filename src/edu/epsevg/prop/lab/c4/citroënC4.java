/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.epsevg.prop.lab.c4;

import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class citroënC4 implements Jugador, IAuto{
    private String nom;
    private int color=1;
    private int prof;
    public int[][] taula_posibilitat = {
        {3, 4, 5, 7, 7, 5, 4, 3},
        {4, 6, 8,10,10, 8, 6, 4},
        {5, 8,11,13,13,11, 8, 5},
        {7,10,13,16,16,13,10, 7},
        {7,10,13,16,16,13,10, 7},
        {5, 8,11,13,13,11, 8, 5},
        {4, 6, 8,10,10, 8, 6, 4},
        {3, 4, 5, 7, 7, 5, 4, 3}
    };
    
    public citroënC4(int prof) {
        this.nom = "citroënC4";
        this.prof = prof;        
      }
    
    @Override
    public String nom() {
        return nom;
    }
    
    @Override
    public int moviment(Tauler t, int color) {
        this.color = color;
        int tirada = nova_tirada(t,prof);
        return tirada;
    }
     
    public int nova_tirada(Tauler t, int profunditat) {
        int millor_heur = Integer.MIN_VALUE;
        int millor_columna = -1;
        for (int i = 0; i < t.getMida(); i++) {
            int alpha = Integer.MIN_VALUE;
            if (t.movpossible(i)) {
                Tauler tauler_aux = new Tauler(t);
                tauler_aux.afegeix(i, this.color);
                if (!tauler_aux.solucio(i, this.color)) {
                    alpha = minimitza(tauler_aux,i, profunditat - 1, millor_heur, Integer.MAX_VALUE);
                    if (alpha > millor_heur || millor_columna == -1) {
                        millor_columna = i;
                        millor_heur = alpha;
                    }
                }
                else {
                    return i;
                }
            }
         }
        return millor_columna;
    }
    
    public int maximitza (Tauler t, int columna ,int profunditat,int alpha,int beta){
        if (profunditat <= 0) {
            return heur(t);
        }
        int nuevaAlfa = Integer.MIN_VALUE;
        for (int i = 0; i < t.getMida(); i++) {
            if (t.movpossible(i)) {
                Tauler taulell_aux = new Tauler(t);
                taulell_aux.afegeix(i, this.color);
                if (!taulell_aux.solucio(i, this.color)) {
                    nuevaAlfa = Math.max(nuevaAlfa, minimitza(taulell_aux,i, profunditat - 1, alpha, beta));
                    alpha = Math.max(nuevaAlfa, alpha);
                    if (alpha >= beta) {
                        return alpha;
                    }
                }
                else {
                    return Integer.MAX_VALUE;
                }
            }
        }
        return nuevaAlfa;
    }
    
    public int minimitza (Tauler t, int columna ,int profunditat,int alpha,int beta){
        if (profunditat <= 0) {
            return heur(t);
        }
        int nuevaBeta = Integer.MAX_VALUE;
        for (int i = 0; i < t.getMida(); i++) {
            if (t.movpossible(i)) {
                Tauler taulell_aux = new Tauler(t);
                taulell_aux.afegeix(i, this.color*-1);
                if (!taulell_aux.solucio(i, this.color*-1)) {
                    nuevaBeta = Math.min(nuevaBeta, maximitza(taulell_aux,i, profunditat - 1, alpha, beta));
                    beta = Math.min(nuevaBeta, beta);
                    if (alpha >= beta) {
                        return beta;
                    }
                }
                else {
                    return Integer.MIN_VALUE;
                }
            }
        }
        return nuevaBeta;
    }
    
        
    public int puntua (Tauler t, int i, int j, int color) {
        int puntuacio = 0;
        if (t.getColor(i, j) != 0) {
            if (t.getColor(i, j) == color) {
                puntuacio += 3;                    
            }
            else {
                puntuacio -= 1;
            }
        }
        return puntuacio;
    }
    
    public int heur(Tauler t) {
        int puntuacio_meva = 0;
        int puntuacio_enemic = 0;
        int size = t.getMida();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (t.getColor(i, j) == this.color) {
                    puntuacio_meva += taula_posibilitat[i][j];
                } else if (t.getColor(i, j) == this.color*-1) {
                    puntuacio_enemic += taula_posibilitat[i][j];
                }
                if (t.getColor(i, j) == this.color) {
                    int hor = puntuacio_horitzontal(t,i,j,this.color);
                    int ver = puntuacio_vertical(t,i,j,this.color);
                    int diag = puntuacio_diagonal(t,i,j,this.color);
                    puntuacio_meva += hor+ver+diag;
                }
                else if (t.getColor(i, j) == this.color*-1) {
                    int hor = puntuacio_horitzontal(t,i,j,this.color*-1);
                    int ver = puntuacio_vertical(t,i,j,this.color*-1);
                    int diag = puntuacio_diagonal(t,i,j,this.color*-1);
                    puntuacio_enemic += hor+ver+diag;  
                }
            }
        }
        return puntuacio_meva - puntuacio_enemic;
    }
    
    public int puntuacio_horitzontal(Tauler t, int i, int j, int color){
        int puntuacio = 0;
        int size = t.getMida();
        for (int x = 0; x < 4; x++) {
            if (i-x < 0 || i+x >= size || j-x < 0 || j+x >= size) {
                break;
            }
            int esq = puntua(t,i-x,j,color);
            int dre = puntua(t,i+x,j,color);
            puntuacio += esq + dre;
        }
        return puntuacio;
    }
    
    public int puntuacio_vertical(Tauler t, int i, int j, int color){
        int puntuacio = 0;
        int size = t.getMida();
        for (int x = 0; x < 4; x++) {
            if (i-x < 0 || i+x >= size || j-x < 0 || j+x >= size) {
                break;
            }
            int punt = puntua(t,i,j+x,color);
            puntuacio += punt;
        }
        return puntuacio;
    }
    
    public int puntuacio_diagonal(Tauler t, int i, int j, int color){
        int puntuacio = 0;
        int size = t.getMida();
        for (int x = 0; x < 4; x++) {
            if (i-x < 0 || i+x >= size || j-x < 0 || j+x >= size) {
                break;
            }
            int esq = puntua(t,i-x,j+x,color);
            int dre = puntua(t,i+x,j+x,color);
            puntuacio += esq + dre;
        }
        return puntuacio;
    }
}

