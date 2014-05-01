package com.AngryStickStudios.StickFlick.Entities;

import com.AngryStickStudios.StickFlick.Controller.AnimationLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class Entity {
	private String name;
	private float healthMax;
	private float healthCurrent;
	private boolean isAlive;
	private float peakamt = 1;
	private boolean changedLayer = false;
	private int splatting = 0;
	AnimationLoader anims;
	Sound dead;

	private int castleMaxHealth;

	public Entity(String name, int healthMax, AnimationLoader ganims){
		this.name = name;
		this.healthMax = healthMax;
		this.healthCurrent = healthMax;
		this.isAlive = true;
		this.anims = ganims;
		
		dead = Gdx.audio.newSound(Gdx.files.internal("data/sounds/enemy_death.mp3"));

	}

	public String getProjFired()
	{
		return "null";
	}

	public int getSplatting()
	{
		return splatting;
	}

	public void setSplatting(int splat)
	{

	}

	public void setState(float state)
	{

	}

	public boolean getChanged()
	{
		return changedLayer;
	}

	public void setPeak(float newamt)
	{

	}

	public void setChanged(boolean change)
	{
		changedLayer = change;
		return;
	}

	public float getPeak()
	{
		return peakamt;
	}

	public void Released(Vector2 speed) {
	}

	public void freeze(){
	}

	public void unfreeze(){
	}

	public void pickedUp() {
	}

	public Entity getTarget(){
		return null;
	}

	public void setTarget(Entity target){

	}
	public Vector2 getPosition()
	{
		return new Vector2();
	}

	public boolean onGround()
	{
		return false;
	}

	public Vector2 getLastPos()
	{
		return new Vector2();
	}

	public Vector2 getGroundPosition()
	{
		return new Vector2();
	}

	public void setPosition(float x, float y){
	}

	public Vector2 getSize(){
		return new Vector2();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getCastleMaxHealth() {
		return castleMaxHealth;
	}

	public void setCastleMaxHealth(int castleMaxHealth) {
		this.castleMaxHealth = castleMaxHealth;
	}

	public float getHealthMax() {
		return healthMax;
	}

	public void setHealthMax(float healthMax) {
		this.healthMax = healthMax;
	}

	public float getHealthCurrent() {
		return healthCurrent;
	}

	public void setHealthCurrent(float healthCurrent) {
		this.healthCurrent = healthCurrent;
	}

	public boolean getIsAlive(){
		return isAlive;
	}

	public void Update(float delta){
	}

	public Image getImage(){
		return new Image();
	}

	public Image getShadow(){
		return new Image();
	}

	public void setIsAlive(boolean isAlive){
		this.isAlive = isAlive;
	}

	public void increaseHealth(int x){
		if(healthCurrent + x >= healthMax)
			healthCurrent = healthMax;
		else
			healthCurrent = healthCurrent + x;

	}

	public void decreaseHealth(int x){
		if(healthCurrent - x <= 0){
			healthCurrent = 0;
			isAlive = false;
			dead.play();
		}
		else
			healthCurrent = healthCurrent - x;
	}

	public void Anim(float delta){

	}

}