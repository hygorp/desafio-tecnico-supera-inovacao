"use client"

import localFont from "next/font/local";
import "./globals.css";
import React from "react";
import {AppContextProvider} from "@/context/app-context-provider";
import {Toaster} from "@/components/ui/toaster";

const geistSans = localFont({
  src: "./fonts/GeistVF.woff",
  variable: "--font-geist-sans",
  weight: "100 900",
});
const geistMono = localFont({
  src: "./fonts/GeistMonoVF.woff",
  variable: "--font-geist-mono",
  weight: "100 900",
});

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
      <AppContextProvider>
        <html lang="en">
          <head>
            <title>Tasks!</title>
          </head>
          <body className={`${geistSans.variable} ${geistMono.variable} antialiased`}>
            <Toaster/>
            {children}
          </body>
        </html>
      </AppContextProvider>
  );
}
