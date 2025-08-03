package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.hod.ultiodivina.Main;

public class LoadingScreen implements Screen {

    private final Main game;
    private final AssetManager assetManager;

    public LoadingScreen(Game game){
        this.game = (Main) game;
        this.assetManager = this.game.assetManager;
    }

    @Override
    public void show() {
        //--- TEXTURES --- //
        //Backgrounds
        assetManager.load("backgrounds/level1/nivel1-1.jpeg", Texture.class);
        assetManager.load("backgrounds/level1/nivel1-2.png", Texture.class);
        assetManager.load("backgrounds/level1/nivel1-3.png", Texture.class);
        assetManager.load("backgrounds/mainMenuBack`ground.png", Texture.class);
        //Buttons
        assetManager.load("buttons/botonContinuar.png", Texture.class);
        assetManager.load("buttons/botonContinuar2.png", Texture.class);
        assetManager.load("buttons/botonJugar.png", Texture.class);
        assetManager.load("buttons/botonJugar2.png", Texture.class);
        assetManager.load("buttons/botonReintentar.png", Texture.class);
        assetManager.load("buttons/botonReintentar2.png", Texture.class);
        assetManager.load("buttons/botonsalir.png", Texture.class);
        assetManager.load("buttons/botonsalir2.png", Texture.class);
        assetManager.load("buttons/botonVolver.png", Texture.class);
        assetManager.load("buttons/botonVolver2.png", Texture.class);
        //Enemies
        assetManager.load("enemies/flyingmouth/bocaConOjo_Expulsion-Sheet.png", Texture.class);
        assetManager.load("enemies/flyingmouth/bocaConOjo_Movimiento-Sheet.png", Texture.class);
        assetManager.load("enemies/flyingmouth/Ojo_Proyectil-Sheet.png", Texture.class);
        assetManager.load("enemies/slime/slime.png", Texture.class);
        assetManager.load("enemies/tentacle/tentaculo-Sheet.png", Texture.class);
        //HUD
        assetManager.load("hud/caliz0Vidas.png", Texture.class);
        assetManager.load("hud/caliz1Vidas.png", Texture.class);
        assetManager.load("hud/caliz2Vidas.png", Texture.class);
        assetManager.load("hud/caliz3Vidas.png", Texture.class);
        //Lymhiel
        assetManager.load("lymhiel/lymhiel_attack.png", Texture.class);
        assetManager.load("lymhiel/lymhiel_die.png", Texture.class);
        assetManager.load("lymhiel/lymhiel_idle.png", Texture.class);
        assetManager.load("lymhiel/lymhiel_jumping.png", Texture.class);
        assetManager.load("lymhiel/lymhiel_projectile.png", Texture.class);
        assetManager.load("lymhiel/lymhiel_walking.png", Texture.class);
        //Platforms
        assetManager.load("platforms/plataformaAereoDerecha.png", Texture.class);
        assetManager.load("platforms/plataformaAereoIzquierda.png", Texture.class);
        assetManager.load("platforms/plataformaAereoLarga.png", Texture.class);
        assetManager.load("platforms/plataformaTierra.png", Texture.class);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }
}
