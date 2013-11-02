<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="BALADE">
<html>
	<head>
		<link rel="stylesheet" href="balades.css"/>
	</head>
	<body>
		<xsl:apply-templates select="PRESENTATION"/>
		<xsl:apply-templates select="RECIT"/>
		<xsl:apply-templates select="COMPLEMENT"/>

	</body>
</html>
</xsl:template>

<xsl:template match="PRESENTATION">
	<h1><xsl:value-of select="TITRE"/></h1>
	<div id="meta">
		<xsl:value-of select="AUTEUR"/><br/>
		<xsl:value-of select="DATE"/><br/>
	</div>
	<div id="desc">
		<xsl:apply-templates select="DESCRIPTION"/><br/>
	</div>
</xsl:template>

<xsl:template match="DESCRIPTION">
	<xsl:apply-templates select="P" mode="desc"/>
</xsl:template>

<xsl:template match="P" mode="desc">
	<p>
		<div class="xpath">( /BALADE[1]/PRESENTATION[1]/DESCRIPTION[1]/P[<xsl:value-of select="position()"/>] )</div>
		<!-- position fonctionne ici car il n'y a que des P dans l'appel au template -->
		<xsl:for-each select="text()|LISTE">
		 <xsl:choose>
			<xsl:when test="name()='LISTE'">
				<xsl:apply-templates select="."/>
			</xsl:when>
			<xsl:otherwise>
			<xsl:value-of select="."/> <br/>
			</xsl:otherwise>
			</xsl:choose> 
	</xsl:for-each>
	</p>
</xsl:template>

<xsl:template match="P" mode="sec">
	<xsl:param name="pos"/>
	<p>
		<div class="xpath"> ( /BALADE[1]/RECIT[1]/SECTION[<xsl:value-of select="$pos"/>]/P[<xsl:number  count='P' format='1' />] )</div>
		
		<xsl:for-each select="text()|LISTE">
		 <xsl:choose>
			<xsl:when test="name()='LISTE'">
				<xsl:apply-templates select="."/>
			</xsl:when>
			<xsl:otherwise>
			<xsl:value-of select="."/> <br/>
			</xsl:otherwise>
			</xsl:choose> 
	</xsl:for-each>
	</p>
</xsl:template>

<xsl:template match="P">
	<p>
		<div class="xpath">( /BALADE[1]/RECIT[1]/P[<xsl:number count='P' format='1' />] ) </div>
		
		<xsl:for-each select="text()|LISTE">
		 <xsl:choose>
			<xsl:when test="name()='LISTE'">
				<xsl:apply-templates select="."/>
			</xsl:when>
			<xsl:otherwise>
			<xsl:value-of select="."/> <br/>
			</xsl:otherwise>
			</xsl:choose> 
	</xsl:for-each>
	</p>
</xsl:template>

<xsl:template match="RECIT">
	<xsl:apply-templates select="SEC|P|PHOTO"/>
</xsl:template>
	
<xsl:template match="SEC">

 <br/>

	<xsl:apply-templates select="SOUS-TITRE"/>
	<xsl:apply-templates select="P|PHOTO" mode="sec">
		<xsl:with-param name="pos">
			<xsl:number level='any' count='SEC' format='1' from='/'/>
			<!-- permet de trouver la position de la section par rapport a la racine -->
		</xsl:with-param>
	</xsl:apply-templates>
</xsl:template>



<xsl:template match="PHOTO" mode="sec">
	<p class="photo">Photo: <xsl:value-of select="."/></p>
</xsl:template>

<xsl:template match="PHOTO">
	<p class="photo">Photo: <xsl:value-of select="."/></p>
</xsl:template>



<xsl:template match="LISTE">
	<ul>
		<xsl:apply-templates select="ITEM"/>
	</ul>
</xsl:template>

<xsl:template match="ITEM">
	<li><xsl:value-of select="."/></li>
</xsl:template>

<xsl:template match="SOUS-TITRE">
	<h2><xsl:value-of select="."/></h2>
</xsl:template>

</xsl:stylesheet>