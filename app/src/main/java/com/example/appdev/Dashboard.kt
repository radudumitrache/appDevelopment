package com.example.appdev

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Dashboard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 100.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Welcome to GoalSaver",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFD9D9D9),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(150.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "John Doe",
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Text(
                    text = "Visa",
                    modifier = Modifier.align(Alignment.TopEnd)
                )
                Text(
                    text = "**** 5794",
                    modifier = Modifier.align(Alignment.BottomStart)
                )
                Text(
                    text = "08/24",
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* todo */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Daily")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { /* todo */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Monthly")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { /* todo */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Yearly")
                }
            }

            Button(
                onClick = { /* todo */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D869C)),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 16.dp)
            ) {
                Text("Add transaction")
            }
        }
    }
}