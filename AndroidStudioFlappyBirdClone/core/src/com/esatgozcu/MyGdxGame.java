package com.esatgozcu;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {

	SpriteBatch batch;

	Texture background;
	Texture bird;
	Texture bullet1;
	Texture bullet2;
	Texture bullet3;

	float width;
	float height;

	float birdX = 0;
	float birdY = 0;

	int gameState = 0;

	float birdVelocity = 0;
	float enemyVelocitiy =11;

	float gravity = 0.5f;

	int numberOfEnemies = 4;

	float [] enemyX;

	float distance = 0;

	float [] enemyOffSet1;
	float [] enemyOffSet2;
	float [] enemyOffSet3;

	Random random;

	Circle birdCircle;

	Circle[] enemyCircle1;
	Circle[] enemyCircle2;
	Circle[] enemyCircle3;

	int score =0;
	int scoredEnemy= 0;

	BitmapFont font;

	//ShapeRenderer shapeRenderer;

	// Program ilk çağrıldığında çalışacak yer.
	@Override
	public void create () {

		batch = new SpriteBatch();
		//Arka planlarımızı ayarlıyoruz.
		background = new Texture("background.png");
		bird = new Texture("bird.png");
		bullet1 = new Texture("bullet.png");
		bullet2 = new Texture("bullet.png");
		bullet3 = new Texture("bullet.png");

		// Score ve Game Over yazdırabilmek için font oluşturuyoruz.
		font= new BitmapFont();
		// Beyaz rengini seçiyoruz.
		font.setColor(Color.WHITE);
		// Yazı büyüklüğünü ayarlıyoruz.
		font.getData().setScale(4);

		// Sürekli bir şekilde oyunun en ve boy oranına ihtiyacımız olduğu için bir değişkene atıyoruz.
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		// Kuşun hangi konumda durması gerektiğini ayarlıyoruz.
		birdX = width/3;
		birdY = height/3;

		// Kurşunlar arasındaki mesafeyi bir değişkene atıyoruz.
		distance = width/2;

		// Kurşunların x ekseninde rastgele gelmesi için dizi oluşturuyoruz.
		enemyX = new float[numberOfEnemies];

		// Kurşunların random(rastgele) bir şekilde gelmesi için nesnemizi türetiyoruz.
		random = new Random();

		// Kurşunların y düzleminde rastgele gelmesi için her kurşun için ayrı ayrı dizi oluşturuyoruz.
		enemyOffSet1 = new float[numberOfEnemies];
		enemyOffSet2 = new float[numberOfEnemies];
		enemyOffSet3 = new float[numberOfEnemies];

		// Çarpışmaları anlayabilmek için kuşun etrafında görünmez bir daire çiziyoruz.
		birdCircle = new Circle();

		// Kurşunların y düzleminde rastgele gelmesi için her kurşun için ayrı ayrı dizi oluşturduğumuz için
		// çemberler içinde ayrı ayrı dizi oluşturuyoruz.
		enemyCircle1 = new Circle[numberOfEnemies];
		enemyCircle2 = new Circle[numberOfEnemies];
		enemyCircle3 = new Circle[numberOfEnemies];

		//shapeRenderer = new ShapeRenderer();

		for (int i =0; i<numberOfEnemies; i++)

		{
			// Kurşunların x ekseninde belli aralıklarla gelmesini ayarlıyoruz.
			enemyX[i] = width +i * distance;

			// Kurşunların y ekseninde rastgele gelmesini ayarlıyoruz.
			// random.nextInt(100) = 0 ile 100 arasında rastgele değer üretir.
			enemyOffSet1[i] = random.nextInt(Gdx.graphics.getHeight());
			enemyOffSet2[i] = random.nextInt(Gdx.graphics.getHeight());
			enemyOffSet3[i] = random.nextInt(Gdx.graphics.getHeight());

			// Görünmez dairelerimizi ayarlıyoruz.
			enemyCircle1[i] = new Circle();
			enemyCircle2[i] = new Circle();
			enemyCircle3[i] = new Circle();
		}
	}

	// Program başladığında sürekli çağrılacak yer.
	@Override
	public void render () {

		batch.begin();

		// Oyun başladığında arkaplanımızı çizdiriyoruz.
		batch.draw(background,0,0,width,height);


		if (gameState==1)
		{

			// Kurşunlar x ekseninde kuşu geçtiği zaman..
			if (enemyX[scoredEnemy]<birdX)
			{
				score++;

				// scoredEnemy 0 ile 2 arasında tutuyoruz.
				if(scoredEnemy<numberOfEnemies-1)
				{
					scoredEnemy++;
				}
				else {
					scoredEnemy =0;
				}
			}

			//Eğer ekrana tıklanırsa..
			if (Gdx.input.justTouched())
			{
				// Kuşun hızını -9 olarak ayarlıyoruz.
				birdVelocity = -9;
			}

			for (int i = 0; i<numberOfEnemies; i++)
			{
				// Kurşunlar ekranın sonuna geldiği zaman tekrarda başlatıyoruz.
				if (enemyX [i]< width /20)
				{
					// Kurşunların x eksenindeki uzaklığını ayarlıyoruz.
					enemyX[i] =width +numberOfEnemies * distance;

					// Kurşunların y ekseninden uzaklıklarını tekrardan ayarlıyoruz.
					enemyOffSet1[i] = random.nextInt(Gdx.graphics.getHeight());
					enemyOffSet2[i] = random.nextInt(Gdx.graphics.getHeight());
					enemyOffSet3[i] = random.nextInt(Gdx.graphics.getHeight());
				}
				else {
					// Eğer ekranın sonuna gelinmediyse kurşunların sürekli hareket etmesi için
					// Kurşunun hızını x ekseninden çıkartıyoruz.
					enemyX[i] = enemyX[i] - enemyVelocitiy;
				}
				// Kurşunları çizdiriyoruz.
				batch.draw(bullet1,enemyX[i],enemyOffSet1[i],width/15,height/12);
				batch.draw(bullet2,enemyX[i],enemyOffSet2[i],width/15,height/12);
				batch.draw(bullet3,enemyX[i],enemyOffSet3[i],width/15,height/12);

				// Görünmez dairelerimizi çizdiriyoruz.
				enemyCircle1[i] = new Circle(enemyX[i]+width/30,enemyOffSet1[i]+height/20,width/25);
				enemyCircle2[i] = new Circle(enemyX[i]+width/30,enemyOffSet2[i]+height/20,width/25);
				enemyCircle3[i] = new Circle(enemyX[i]+width/30,enemyOffSet3[i]+height/20,width/25);
			}

			if (birdY >0)
			{
				// Kuş y ekseninde 0 dan yukarıysa yani yere düşmediyse
				// Kuşun hızından yerçekimini çıkartıyoruz.
				birdVelocity = birdVelocity + gravity;
				// Kuşun y ekseninden kuşun hızını çıkartıyoruz.
				birdY = birdY - birdVelocity;
			}
			else {
				// Kuş yere düşerse..
				gameState =2;
			}
		}
		else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		}
		else  if (gameState == 2)
		{
			// Oyun biterse yani kuş yere düşerse veya kurşuna çarparsa..
			// Game Over ve Tap to Play Again! ekrana yazdırıyoruz.
			font.draw(batch,"Game Over!",height/2,width/3);
			font.draw(batch,"Tap to Play Again!",height/3,width/4);

			// Oyun bittikten sonra ekrana tıklanırsa..
			if(Gdx.input.justTouched())
			{
				// Scoru ve kuşun hızını sıfırlıyoruz.
				score =0;
				birdVelocity =0;

				gameState =1;

				// Kuşu başlangıçtaki haline getiriyoruz
				birdY= height/2;

				// Kurşunları başlangıçtaki haline getiriyoruz.
				for (int i =0; i<numberOfEnemies; i++)

				{
					enemyX[i] = width +i * distance;

					enemyOffSet1[i] = random.nextInt(Gdx.graphics.getHeight());
					enemyOffSet2[i] = random.nextInt(Gdx.graphics.getHeight());
					enemyOffSet3[i] = random.nextInt(Gdx.graphics.getHeight());

					enemyCircle1[i] = new Circle();
					enemyCircle2[i] = new Circle();
					enemyCircle3[i] = new Circle();
				}
			}

			scoredEnemy =0;

		}
		// Kuşu çizdiriyoruz.
		// draw(kuşun arkapalanı, kuşun x ekseni, kuşun y ekseni, kuşun eni,kuşun boyu)
		batch.draw(bird,birdX,birdY,width/15,height/12);

		// Skor tabelasını ekrana yazdırıyoruz.
		font.draw(batch,String.valueOf(score),width/2,height/2+height/3);

		batch.end();
		// Kuşun görünmez çemberini çizdiriyoruz.
		birdCircle.set(birdX+width/30,birdY+height/24,width/30);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLACK);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

		for (int i=0; i < numberOfEnemies; i++)
		{
			//shapeRenderer.circle(enemyX[i]+width/30,enemyOffSet1[i]+height/20,width/20);
			//shapeRenderer.circle(enemyX[i]+width/30,enemyOffSet2[i]+height/20,width/20);
			//shapeRenderer.circle(enemyX[i]+width/30,enemyOffSet3[i]+height/20,width/20);

			// Eğer kurşun ile kuş çarpışırsa...
			if(Intersector.overlaps(birdCircle,enemyCircle1[i])||Intersector.overlaps(birdCircle,enemyCircle2[i])||Intersector.overlaps(birdCircle,enemyCircle3[i]))
			{
				gameState = 2;
			}
		}
		//shapeRenderer.end();
	}
	@Override
	public void dispose () {
	}
}