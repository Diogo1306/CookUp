import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getAnalytics } from "firebase/analytics";

const firebaseConfig = {
  apiKey: "AIzaSyBO74akVBMnqoTolGokNQfmA3GM6U1ODDA",
  authDomain: "cookup-bc12b.firebaseapp.com",
  databaseURL: "https://cookup-bc12b-default-rtdb.europe-west1.firebasedatabase.app",
  projectId: "cookup-bc12b",
  storageBucket: "cookup-bc12b.firebasestorage.app",
  messagingSenderId: "279648868565",
  appId: "1:279648868565:web:5cc09b43debf43d069ea5c",
  measurementId: "G-CT96J562NF",
};

const app = initializeApp(firebaseConfig);

export const auth = getAuth(app);

const analytics = getAnalytics(app);
