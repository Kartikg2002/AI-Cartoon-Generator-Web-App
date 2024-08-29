<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <meta charset="ISO-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI Cartoon Generator</title>
    <link rel="stylesheet" href="homeStyle.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
</head>
<body>
    <header>
        <div class="logo">
            <img src="/images/logo.jfif" alt="AI Cartoon Logo">
            <h1>${appName}</h1>
        </div>
        <nav class="navbar">
            <ul>
                <li><a href="home">Home</a></li>
            </ul>
        </nav>
    </header>
    <main>
        <section class="intro-section">
            <div class="intro-text">
                <h2>Transform Your Photos into Cartoons!</h2>
                <p>Welcome to the AI Cartoon Generator, where your images come to life as captivating cartoon masterpieces. Harnessing the power of advanced artificial intelligence, our tool seamlessly converts your cherished photos into vibrant cartoons with incredible detail and artistic flair.</p>
                <p>Supported Image Formats: <strong>JPEG, PNG, JPG, BMP, WEBP</strong>. Max Image Size: <strong>10 MB</strong>. Input Dimensions: <strong>256x256 to 5760x3240 pixels</strong>. Output Dimensions: Short side is <strong>1536 pixels</strong>. If the input aspect ratio is ≤ <strong>1.5:1</strong>, the original ratio is maintained. Otherwise, adaptive cropping to achieve a <strong>1.5:1</strong> aspect ratio is applied.</p>
            </div>
            <div class="intro-image">
                <img src="/images/MainCartoon.jfif" alt="Cartoon Example">
            </div>
        </section>

        <!-- Side-by-side Form and Image Section -->
        <section class="main-content">
            <div class="form-section">
                <c:if test="${result!=null}">
                    <p class="result-message">${result}</p>
                </c:if>
                <form action="generate1" method="post" enctype="multipart/form-data" class="upload-form">
                    <label for="upload" class="upload-label">Upload Your Photo:</label>
                    <input type="file" accept="image/*" name="image" id="upload" required class="upload-input" />
                    <button type="submit" class="submit-button">Generate Cartoon</button>
                </form>
            </div>
            <div class="image-section">
                <h3>Generated Cartoon Image</h3>
                <img class="result-image" alt="Generated Cartoon Image" src="${result_url}" />
                <!-- Download Button -->
                <c:if test="${result_url != null}">
                    <a href="${result_url}" download="cartoon_image.jpg" class="download-button">Download Image</a>
                </c:if>
            </div>
        </section>

        <!-- New Demo Images Section -->
        <section class="demo-section">
            <h3>Demo Images</h3>
            <div class="demo-images">
                <img class="demo-image" src="/images/Cartoon.jfif" alt="Demo 1" />
                <img class="demo-image" src="/images/Cartoon2.jfif" alt="Demo 2" />
                <img class="demo-image" src="/images/Cartoon3.jfif" alt="Demo 3" />
                <img class="demo-image" src="/images/Cartoon4.jfif" alt="Demo 1" />
                <img class="demo-image" src="/images/Cartoon5.jfif" alt="Demo 2" />
                <img class="demo-image" src="/images/Cartoon6.jfif" alt="Demo 3" />
                <img class="demo-image" src="/images/Cartoon7.jfif" alt="Demo 1" />
                <img class="demo-image" src="/images/Cartoon8.jfif" alt="Demo 2" />
            </div>
        </section>
    </main>
    <footer>
        <p>© 2024 AI Cartoon Generator. All Rights Reserved.</p>
    </footer>
</body>
</html>
