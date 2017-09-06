const path = require('path');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const webpack = require('webpack');

const outputFolder = 'dist';

module.exports = {
    entry: {
        index: './index.js'
    },
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, outputFolder)
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                loader: 'babel-loader',
                query: {
                    presets: ['babel-preset-es2015']
                }
            },
            {
                test: /\.css$/,
                use: [
                    {loader: 'style-loader'},
                    {loader: 'css-loader'}
                ]
            },
            {test: /\.(png|woff|woff2|eot|ttf|svg)$/, loader: 'url-loader?limit=100000'}
        ]
    },
    plugins: [
        new CopyWebpackPlugin([
            {from: 'index.html', to: 'index.html'},
        ]),
        new webpack.optimize.UglifyJsPlugin({
            compress: {warnings: false}
        })
    ]
};
