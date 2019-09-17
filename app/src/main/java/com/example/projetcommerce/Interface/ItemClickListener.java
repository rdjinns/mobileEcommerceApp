package com.example.projetcommerce.Interface;

import android.view.View;
// va servir pour move l'user dans le produit détaillé
public interface ItemClickListener {

    void onClick(View view, int position, boolean isLongClick);

}
