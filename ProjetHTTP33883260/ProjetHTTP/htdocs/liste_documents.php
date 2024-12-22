<?php
$dir = './htdocs';
$files = scandir($dir);
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Documents</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f8ff;
            color: #333;
            text-align: center;
            padding: 50px;
        }
        a {
            display: block;
            margin: 10px 0;
            padding: 10px;
            background-color: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        a:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
    <h1>Liste des Documents</h1>
    <?php foreach ($files as $file): ?>
        <?php if ($file !== '.' && $file !== '..' ): ?>
            <?php
                // Vérifiez si index.php ou index.html existe dans le dossier
                $indexPhp = $file . '/index.php';
                $indexHtml = $file . '/index.html';
                $link = '';

                if (file_exists($dir . '/' . $indexPhp)) {
                    $link = $indexPhp;
                } elseif (file_exists($dir . '/' . $indexHtml)) {
                    $link = $indexHtml;
                } else {
                    $link = $file; // Si aucun index n'existe, utilisez le nom du dossier
                }
            ?>
            <a href="<?php echo $link; ?>"><?php echo $file; ?></a>
        <?php endif; ?>
    <?php endforeach; ?>
    <a href="welcome.html">Retour à l'accueil</a>
</body>
</html>