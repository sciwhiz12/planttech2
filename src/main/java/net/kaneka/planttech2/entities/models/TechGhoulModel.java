package net.kaneka.planttech2.entities.models;// Made with Blockbench 3.5.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TechGhoulModel<T extends Entity> extends EntityModel<T>
{
	private final ModelRenderer Jaw;
	private final ModelRenderer Jaw2;
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private final ModelRenderer bone3;
	private final ModelRenderer bone4;
	private final ModelRenderer bone5;
	private final ModelRenderer body;

	public TechGhoulModel()
	{
		textureWidth = 64;
		textureHeight = 64;

		Jaw = new ModelRenderer(this);
		Jaw.setRotationPoint(0.0F, 3.4264F, -0.1808F);
		setRotationAngle(Jaw, 0.6109F, 0.0F, 0.0F);
		Jaw.setTextureOffset(43, 0).addBox(-3.0F, -3.2119F, -4.672F, 6.0F, 1.0F, 2.0F, 0.0F, false);
		Jaw.setTextureOffset(0, 1).addBox(-3.0F, -4.2119F, -4.672F, 0.0F, 1.0F, 2.0F, 0.0F, false);
		Jaw.setTextureOffset(0, 0).addBox(3.0F, -4.2119F, -4.672F, 0.0F, 1.0F, 2.0F, 0.0F, false);
		Jaw.setTextureOffset(13, 21).addBox(2.0F, -4.2119F, -4.672F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		Jaw.setTextureOffset(0, 21).addBox(-3.0F, -4.2119F, -4.672F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		Jaw.setTextureOffset(0, 0).addBox(-1.0F, -4.2119F, -4.672F, 2.0F, 1.0F, 0.0F, 0.0F, false);

		Jaw2 = new ModelRenderer(this);
		Jaw2.setRotationPoint(0.0F, 25.342F, -5.0603F);
		setRotationAngle(Jaw2, -0.3491F, 0.0F, 0.0F);
		Jaw2.setTextureOffset(28, 41).addBox(-3.0F, -23.2557F, -9.2214F, 6.0F, 1.0F, 2.0F, 0.0F, false);
		Jaw2.setTextureOffset(0, 22).addBox(-3.0F, -22.2557F, -9.2214F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		Jaw2.setTextureOffset(9, 19).addBox(-3.0F, -22.2557F, -9.2214F, 0.0F, 1.0F, 2.0F, 0.0F, false);
		Jaw2.setTextureOffset(0, 2).addBox(3.0F, -22.2557F, -9.2214F, 0.0F, 1.0F, 2.0F, 0.0F, false);
		Jaw2.setTextureOffset(0, 1).addBox(-1.0F, -22.2557F, -9.2214F, 2.0F, 1.0F, 0.0F, 0.0F, false);
		Jaw2.setTextureOffset(21, 21).addBox(2.0F, -22.2557F, -9.2214F, 1.0F, 1.0F, 0.0F, 0.0F, false);

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 22.0F, 14.0F);
		setRotationAngle(bone, 0.6109F, 0.0F, 0.0F);
		bone.setTextureOffset(40, 31).addBox(2.0F, -26.0F, 2.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(-6.0F, 22.0F, 14.0F);
		setRotationAngle(bone2, 0.6109F, 0.0F, 0.0F);
		bone2.setTextureOffset(40, 40).addBox(2.0F, -26.0F, 2.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(-6.0F, 31.0F, 14.0F);
		setRotationAngle(bone3, 0.6109F, 0.0F, 0.0F);
		bone3.setTextureOffset(19, 40).addBox(2.0F, -26.0F, 2.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(0.0F, 31.0F, 14.0F);
		setRotationAngle(bone4, 0.6109F, 0.0F, 0.0F);
		bone4.setTextureOffset(36, 24).addBox(2.0F, -26.0F, 2.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(-3.0F, 27.0F, 14.0F);
		setRotationAngle(bone5, 0.6109F, 0.0F, 0.0F);
		bone5.setTextureOffset(31, 34).addBox(2.0F, -26.0F, 2.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		body.setTextureOffset(34, 0).addBox(1.0F, -12.0F, -2.0F, 3.0F, 12.0F, 3.0F, 0.0F, false);
		body.setTextureOffset(24, 24).addBox(-4.0F, -12.0F, -2.0F, 3.0F, 12.0F, 3.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addBox(-6.0F, -28.0F, -3.0F, 12.0F, 16.0F, 5.0F, 0.0F, false);
		body.setTextureOffset(12, 21).addBox(-9.0F, -27.0F, -2.0F, 3.0F, 21.0F, 3.0F, 0.0F, false);
		body.setTextureOffset(0, 21).addBox(6.0F, -27.0F, -2.0F, 3.0F, 21.0F, 3.0F, 0.0F, false);
		body.setTextureOffset(34, 15).addBox(-3.0F, -27.0F, -5.0F, 6.0F, 7.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		Jaw.render(matrixStack, buffer, packedLight, packedOverlay);
		Jaw2.render(matrixStack, buffer, packedLight, packedOverlay);
		bone.render(matrixStack, buffer, packedLight, packedOverlay);
		bone2.render(matrixStack, buffer, packedLight, packedOverlay);
		bone3.render(matrixStack, buffer, packedLight, packedOverlay);
		bone4.render(matrixStack, buffer, packedLight, packedOverlay);
		bone5.render(matrixStack, buffer, packedLight, packedOverlay);
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}