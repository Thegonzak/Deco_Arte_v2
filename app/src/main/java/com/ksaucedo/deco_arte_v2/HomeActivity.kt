package com.ksaucedo.deco_arte_v2


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ksaucedo.deco_arte_v2.adapter.CarritoRecyclerAdapter
import com.ksaucedo.deco_arte_v2.adapter.NovedadRecyclerAdapter
import com.ksaucedo.deco_arte_v2.adapter.ProductoRecyclerAdapter
import com.ksaucedo.deco_arte_v2.model.Carrito
import com.ksaucedo.deco_arte_v2.model.Novedad
import com.ksaucedo.deco_arte_v2.model.Producto
import com.ksaucedo.deco_arte_v2.model.Usuario
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType {
    BASIC
}

class HomeActivity : AppCompatActivity() {
    private lateinit var usuario: Usuario
    private var email: String? = null
    var admin = false
    private val productos = ArrayList<Producto>()
    private var novedades = ArrayList<Novedad>()
    private val carritos = ArrayList<Carrito>()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var productoRecyclerAdapter: ProductoRecyclerAdapter
    private lateinit var novedadRecyclerAdapter: NovedadRecyclerAdapter
    private lateinit var carritoRecyclerAdapter: CarritoRecyclerAdapter
    private val productoFragmento = ProductoFragmento()
    private val novedadFragmento = NovedadFragmento()
    private val carritoFragmento = CarritoFragmento()
    private val cuentaFragmento = CuentaFragmento(null)
    private val nuevaNovedad = NuevaNovedad()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        // Cambiar color de la barra de estado
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.resources.getColor(R.color.naranja, null)

        val bundle = intent.extras
        email = bundle?.getString("email")

        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.apply()

        getDataCollection()


        //LLAMAR AL FRAGMENTO
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.inicioMenu -> {
                    setCurrentFragment(novedadFragmento)
                    true
                }
                R.id.explorarMenu -> {
                    setCurrentFragment(productoFragmento)
                    true
                }
                R.id.contactarMenu -> {
                    setCurrentFragment(carritoFragmento)
                    true
                }
                R.id.cuentaMenu -> {
                    setCurrentFragment(cuentaFragmento)
                    true
                }
                else -> false
            }
        }
    }

    // Funcion para mostar los fragmentos
    private fun setCurrentFragment(fragmento: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerView, fragmento)
            commit()
        }
    }

    private fun getProductos() {
        db.collection("productos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    productos.add(
                        Producto(
                            document.getLong("id")!!.toInt(),
                            document.get("descripcion").toString(),
                            document.get("categoria").toString(),
                            document.get("precio").toString(),
                            document.get("imagen").toString(),
                        )
                    )
                }
            }

    }

    private fun getUser() {
        db.collection("usuarios")
            .document(email.toString())
            .get()
            .addOnCompleteListener { user ->
                val register = Intent(this, RegisterActivity::class.java).apply {
                    putExtra("email", email)
                }
                if (user.isSuccessful) {
                    val document = user.result
                    if (document != null) {
                        if (document.exists()) {
                            val prefs =
                                getSharedPreferences(
                                    getString(R.string.prefs_file),
                                    Context.MODE_PRIVATE
                                ).edit()
                            admin = document.getBoolean("admin") == true
                            novedadFragmento.admin = admin
                            prefs.putBoolean("admin", admin)
                            prefs.apply()
                            cuentaFragmento.usuario = Usuario(
                                document.getString("cedula").toString(),
                                document.getString("nombre").toString(),
                                document.getString("apellido").toString(),
                                document.getString("ciudad").toString(),
                                document.getString("direccion").toString(),
                                document.getString("fechaNac").toString(),
                                document.getString("telefono").toString()
                            )

                        } else {
                            startActivity(register)
                        }
                    } else {
                        startActivity(register)
                    }
                }
            }

    }

    private fun getDataCollection() {
        getUser()
        getProductos()
        getCarritos()
        getConfig()
    }

    private fun getConfig() {
        db.collection("config")
            .document("app")
            .get()
            .addOnCompleteListener { user ->

                if (user.isSuccessful) {
                    val document = user.result
                    if (document != null) {
                        if (document.exists()) {
                            carritoFragmento.numero = document.getString("whatsapp").toString()
                        }
                    }
                }
            }
    }


    fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()

        val authActivity = Intent(this, AuthActivity::class.java)
        startActivity(authActivity)
    }

    fun initProductoRecyclerView() {
        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE)
        val admin = prefs.getBoolean("admin", false)
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewProductos)
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingDecorator)
            productoRecyclerAdapter = ProductoRecyclerAdapter(this@HomeActivity)
            adapter = productoRecyclerAdapter
        }
        productoRecyclerAdapter.admin = admin
        productoRecyclerAdapter.submitList(productos)
    }

    fun initCarritoRecyclerView() {

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewCarrito)
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingDecorator)
            carritoRecyclerAdapter = CarritoRecyclerAdapter()
            adapter = carritoRecyclerAdapter
        }
        carritoRecyclerAdapter.submitList(carritos)
    }

    fun initNovedadRecyclerView() {
        val prefs =
            getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE)
        val admin = prefs.getBoolean("admin", false)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerViewNovedades)
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingDecorator)
            novedadRecyclerAdapter = NovedadRecyclerAdapter()
            adapter = novedadRecyclerAdapter
        }
        novedadRecyclerAdapter.admin = admin
        getNovedades()

        findViewById<Button>(R.id.agregarNovedad).setOnClickListener {
            setCurrentFragment(nuevaNovedad)
        }
    }

    fun eliminarNovedad(id: String) {
        db.collection("novedades").document(id).delete()
        getNovedades()
    }

    fun ediatrNovedad(id: String) {
        nuevaNovedad.id = id
        setCurrentFragment(nuevaNovedad)
    }

    private fun getNovedades() {
        db.collection("novedades")
            .get()
            .addOnSuccessListener { result ->
                this.novedades.clear()
                for (document in result) {
                    this.novedades.add(
                        Novedad(
                            document.id,
                            document.get("texto").toString(),
                            true,
                            Timestamp.now(),
                            Timestamp.now(),
                        )
                    )
                }
                novedadRecyclerAdapter.submitList(this.novedades);
                novedadRecyclerAdapter.notifyItemRangeChanged(0, this.novedades.size)
            }
    }

    private fun getCarritos() {
        db.collection("carritos")
            .whereEqualTo("usuario", email)
            .get()
            .addOnSuccessListener { result ->
                carritos.clear()
                for (document in result) {
                    carritos.add(
                        Carrito(
                            document.getLong("id")!!.toInt(),
                            document.get("descripcion").toString(),
                            document.get("categoria").toString(),
                            document.get("precio").toString(),
                        )
                    )
                }
            }
    }

    fun goToHome() {
        setCurrentFragment(novedadFragmento)
    }

    fun agregarCarrito(producto: Producto) {
        Toast.makeText(applicationContext, "Producto agregado", Toast.LENGTH_SHORT).show()

        val find = carritos.find {
            it.id == producto.id
        }
        if (find == null) {
            carritos.add(
                Carrito(
                    producto.id,
                    producto.descripcion,
                    producto.categoria,
                    producto.precio,
                )
            )

            val carrito = hashMapOf(
                "id" to producto.id,
                "descripcion" to producto.descripcion,
                "categoria" to producto.categoria,
                "precio" to producto.precio,
                "usuario" to email,
            )
            db.collection("carritos")
                .add(carrito)
        }
    }

    fun getCarritosList(): ArrayList<Carrito> {
        return carritos
    }

    fun filterProductos(filterText: String) {
        val filteredList = productos.filter {
            it.descripcion.contains(filterText, true)
        }
        productoRecyclerAdapter.submitList(filteredList)
    }

    fun eliminarCarrito(id: Int) {
        val find = carritos.find {
            it.id == id
        }
        if (find != null) {
            carritos.remove(find)
            carritoRecyclerAdapter.submitList(carritos)
            db.collection("carritos")
                .whereEqualTo("id", id)
                .whereEqualTo("usuario", email)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        db.collection("carritos").document(document.id).delete()
                    }
                }
        }
    }

    fun limpiarCarrito() {
        carritos.clear()
        carritoRecyclerAdapter.submitList(carritos)
        db.collection("carritos")
            .whereEqualTo("usuario", email)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("carritos").document(document.id).delete()
                }
            }

    }

}