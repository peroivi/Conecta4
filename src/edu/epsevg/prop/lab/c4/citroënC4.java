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
    private boolean primerMove;
    private int contaNodes;
    private final ArrayList<int[]> direcciones;
    
    public citroënC4(int prof) {
        this.nom = "citroënC4";
        this.prof = prof;
        this.contaNodes = 0;
        this.primerMove = true;
        
        this.direcciones = new ArrayList<>();
        direcciones.add(new int[] { 1, 0 });
        direcciones.add(new int[] { 1, 1 });
        direcciones.add(new int[] { 0, 1 });
        direcciones.add(new int[] { -1, 1 });
        direcciones.add(new int[] { -1, 0 });
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
        int mejorHeur = Integer.MIN_VALUE;
        int millor_columna = -1;
        for (int i = 0; i < t.getMida(); i++) {
            int alpha = Integer.MIN_VALUE;
            if (t.movpossible(i)) {
                Tauler tauler_aux = new Tauler(t);
                tauler_aux.afegeix(i, this.color);
                if (!tauler_aux.solucio(i, this.color)) {
                    alpha = minimitza(tauler_aux,i, profunditat - 1, mejorHeur, Integer.MAX_VALUE);
                    if (alpha > mejorHeur || millor_columna == -1) {
                        millor_columna = i;
                        mejorHeur = alpha;
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
    
    
    //-----------------------------------------------------------------------------------------
    
     protected int largo (Tauler t, int i, int j, int direccionX, int direccionY, int color) {
        int size = t.getMida();
        int score = 0;
        for (int k = 0; k < 4; k++) {
            i += direccionX;
            j += direccionY;
            if (i < 0 || i >= size || j < 0 || j >= size) {
                break;
            }
            int colorPos = t.getColor(i, j);
            if (colorPos == 0) {
                score += 3;
            }
            else {
                if (colorPos == color) {
                    score += 2;                    
                }
                else {
                    score -= 1;
                }
            }
        }
        return score;
    }
    
     /**
     * Metodo que calcula la heurística de un tablero entero. Para calcular la heurística, 
     * comprueva todas las fichas del tablero y le da prioridad aquellas fichas que esten
     * juntas. 
     * 
     * @param t es el tablero que esta actualmente en la partida.
     * 
     * @return puntos que indica cual de buena es la jugada que se está planteando. 
     * 
     * @see largo(Tauler t, int i, int j, int direccionX, int direccionY, int color) 
     */
    protected int heur(Tauler t) {

        
        int puntosYo = 0;
        int puntosEnemigo = 0;
        int size = t.getMida();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (t.getColor(i, j) == this.color) {
                    for (int[] direc : direcciones) {
                        int dirX = direc[0];
                        int dirY = direc[1];
                        int puntos = largo(t, i, j, dirX, dirY, this.color);
                        puntosYo += puntos;
                    }
                }
                else if (t.getColor(i, j) == this.color*-1) {
                    for (int[] direc : direcciones) {
                        int dirX = direc[0];
                        int dirY = direc[1];
                        int puntos = largo(t, i, j, dirX, dirY, this.color*-1);
                        puntosEnemigo += puntos;
                    }
                }
            }
        }
        return puntosYo - puntosEnemigo;
    }
}

