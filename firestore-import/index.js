import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import { collection, addDoc } from "firebase/firestore";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyAWikN0rUsFuDdkz4dTm1o28YZbYMw1a0o",
  authDomain: "decoartedb.firebaseapp.com",
  databaseURL: "https://decoartedb-default-rtdb.firebaseio.com",
  projectId: "decoartedb",
  storageBucket: "decoartedb.appspot.com",
  messagingSenderId: "822389760417",
  appId: "1:822389760417:web:729d633cbc3f04e76c2be0",
  measurementId: "G-Z8YM5DRZ13",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const db = getFirestore(app);


import invenatrio from './inventario.json' assert {type: 'json'};

const main = async () => {
  invenatrio.forEach(async function (obj) {
    try {
      const docRef = await addDoc(collection(db, "productos"), {
        id: obj.id,
        descripcion: obj.descripcion,
        categoria: obj.categoria,
        precio: obj.precio,
        imagen: obj.Imagen
      });

      console.log("Document written with ID: ", docRef.id, obj.descripcion);
    } catch (e) {
      console.error("Error adding document: ", e);
    }
  });
};

main();
